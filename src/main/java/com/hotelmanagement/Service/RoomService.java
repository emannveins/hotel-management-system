
package com.hotelmanagement.Service;

import com.hotelmanagement.dto.room.CreateRoomRequest;
import com.hotelmanagement.dto.room.RoomResponse;
import com.hotelmanagement.dto.room.RoomType;
import com.hotelmanagement.entity.RoomEntity;
import com.hotelmanagement.enums.RoomStatus;
import com.hotelmanagement.enums.ReservationStatus;
import com.hotelmanagement.repository.RoomRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    // recupera tutte le camere 
    public List<RoomResponse> getAllRooms() {
        return roomRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // recupera camere disponibili
    public List<RoomResponse> getAvailableRooms( LocalDate checkIn, LocalDate checkOut, RoomType type) {

        // controllo validita dati
        if (checkIn == null || checkOut == null ||
                !checkOut.isAfter(checkIn)) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Le date della prenotazione non sono valide"
            );
        }

        // ricerca delle camere disponibili
        List<RoomEntity> availableRooms = roomRepository.findAvailableRooms( checkIn, checkOut, type, RoomStatus.MAINTENANCE, ReservationStatus.CANCELLED);

        // conversione Entity -> Response 
        return availableRooms.stream()
                .map(this::toResponse)
                .toList();
    }

    // crea una nuova camera
    public RoomResponse createRoom(CreateRoomRequest request) {

        // Controllo numero camera
        if (request.roomNumber() == null ||
                request.roomNumber().isBlank()) { throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Il numero della camera è obbligatorio"
            );
        }

        String roomNumber = request.roomNumber().trim();

        // controllo camera già esistente
        if (roomRepository.existsByRoomNumber(roomNumber)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Questa camera esiste già"
            );
        }

        // creazione entity
        RoomEntity room = new RoomEntity();
        room.setRoomNumber(roomNumber);
        room.setType(request.type());
        room.setPricePerNight(request.pricePerNight());
        room.setStatus(RoomStatus.AVAILABLE);
        RoomEntity savedRoom = roomRepository.save(room);
        return toResponse(savedRoom);
    }

    // metodo di supporto: Entity -> Response DTO
    private RoomResponse toResponse(RoomEntity room) {
        return new RoomResponse(
                room.getId(),
                room.getRoomNumber(),
                room.getType(),
                room.getPricePerNight(),
                room.getStatus()
        );
    }
}
