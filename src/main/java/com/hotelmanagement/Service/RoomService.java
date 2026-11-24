
package com.hotelmanagement.Service;


import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.hotelmanagement.dto.CreateRoomRequest;
import com.hotelmanagement.dto.RoomResponse;
import com.hotelmanagement.entity.ReservationEntity;
import com.hotelmanagement.entity.RoomEntity;
import com.hotelmanagement.enums.ReservationStatus;
import com.hotelmanagement.enums.RoomStatus;
import com.hotelmanagement.enums.RoomType;
import com.hotelmanagement.repository.ReservationRepository;
import com.hotelmanagement.repository.RoomRepository;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;

    public RoomService(RoomRepository roomRepository, ReservationRepository reservationRepository) {
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
    }

    // recupera tutte le camere 
    public List<RoomResponse> getAllRooms() {
        return roomRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // recupera camere disponibili
    @Transactional(readOnly = true)
    public List<RoomResponse> getAvailableRooms(
        LocalDate checkIn,
        LocalDate checkOut,
        RoomType type) {

    // controllo date
    if (checkIn == null || checkOut == null ||
            !checkOut.isAfter(checkIn)) {

        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Date non valide"
        );
    }

    // recupero prenotazioni sovrapposte
    List<ReservationEntity> reservations =
            reservationRepository.findByStatusNotAndCheckInBeforeAndCheckOutAfter(
                            ReservationStatus.CANCELLED,
                            checkOut,
                            checkIn
                    );

    // ricavo gli ID delle camere già prenotate
    Set<Long> reservedRoomIds = reservations.stream()
            .map(reservation -> reservation.getRoom().getId())
            .collect(Collectors.toSet());

    // recupero le camere non in manutenzione
    List<RoomEntity> availableRooms =
            roomRepository.findByStatusNot(RoomStatus.MAINTENANCE);

    // riscludo quelle prenotate e applico il filtro tipologia
    return availableRooms.stream()
            .filter(room -> !reservedRoomIds.contains(room.getId()))
            .filter(room -> type == null || room.getType() == type)
            .map(this::toResponse)
            .toList();
    }

    // crea una nuova camera
    public RoomResponse createRoom(CreateRoomRequest request) {

        // Controllo numero camera
        if (request.number() == null ||
                request.number().isBlank()) { throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Il numero della camera è obbligatorio"
            );
        }

        String roomNumber = request.number().trim();

        // controllo camera già esistente
        if (roomRepository.existsByNumber(roomNumber)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Questa camera esiste già"
            );
        }

        // creazione entity
        RoomEntity room = new RoomEntity();
        room.setNumber(roomNumber);
        room.setType(request.type());
        room.setNightlyRate(request.nightlyRate());
        room.setStatus(RoomStatus.AVAILABLE);
        RoomEntity savedRoom = roomRepository.save(room);
        return toResponse(savedRoom);
    }

    // metodo di supporto: Entity -> Response DTO
    // Conversione Entity -> Response DTO
    private RoomResponse toResponse(RoomEntity room) {
        return new RoomResponse(
                room.getId(),
                room.getNumber(),
                room.getType(),
                room.getNightlyRate(),
                room.getCapacity(),
                room.getStatus().name()
        );
    }
    
}
