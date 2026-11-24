package com.hotelmanagement.hotel_management_system.Service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service 
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final CustomerRepository customerRepository;
    private final RoomRepository roomRepository;

    public ReservationService( ReservationRepository reservationRepository, CustomerRepository customerRepository, RoomRepository roomRepository) {
        this.reservationRepository = reservationRepository;
        this.customerRepository = customerRepository;
        this.roomRepository = roomRepository;
    }

     // creazione prenotazione con una transactional
    @Transactional
    public ReservationResponse createReservation(
            CreateReservationRequest request) {

        // controllo delle date
        if (request.checkIn() == null ||  request.checkOut() == null ||!request.checkOut().isAfter(request.checkIn())) {
            throw new ResponseStatusException( HttpStatus.BAD_REQUEST, " date della prenotazione non sono valide");
        }

        if (request.guests() == null || request.guests() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,  "Il numero di ospiti deve essere positivo");
        }

        // recupero cliente
        CustomerEntity customer = customerRepository
                .findById(request.customerId())
                .orElseThrow(() -> new ResponseStatusException( HttpStatus.NOT_FOUND, "cliente non trovato"));

        // Recupero camera
        RoomEntity room = roomRepository
                .findById(request.roomId())
                .orElseThrow(() -> new ResponseStatusException( HttpStatus.NOT_FOUND, "camera non trovata"));

        // Controllo manutenzione
        if (room.getStatus() == RoomStatus.MAINTENANCE) {
            throw new ResponseStatusException( HttpStatus.CONFLICT, "camera in manutenzione");
        }


    }

}
