package com.hotelmanagement.Service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

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
    public ReservationResponse createReservation( CreateReservationRequest request) {

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

        // recupero camera
        RoomEntity room = roomRepository
                .findById(request.roomId())
                .orElseThrow(() -> new ResponseStatusException( HttpStatus.NOT_FOUND, "camera non trovata"));

        // controllo manutenzione
        if (room.getStatus() == RoomStatus.MAINTENANCE) {
            throw new ResponseStatusException( HttpStatus.CONFLICT, "camera in manutenzione");
        }

        // controllo prenotazioni sovrapposte
        boolean alreadyBooked = reservationRepository
                        .existsByRoom_IdAndStatusNotAndCheckInBeforeAndCheckOutAfter(
                                room.getId(),
                                ReservationStatus.CANCELLED,
                                request.checkOut(),
                                request.checkIn()
                        );

        if (alreadyBooked) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Camera non disponibile nel periodo richiesto"
            );
        }

        // Calcolo numero di notti
        long nights = ChronoUnit.DAYS.between(
                request.checkIn(),
                request.checkOut()
        );

        // calcolo prezzo totale
        BigDecimal totalPrice = room.getPricePerNight()
                .multiply(BigDecimal.valueOf(nights));

        // creazione prenotazione
        ReservationEntity reservation = new ReservationEntity();

        reservation.setCustomer(customer);
        reservation.setRoom(room);
        reservation.setCheckIn(request.checkIn());
        reservation.setCheckOut(request.checkOut());
        reservation.setGuests(request.guests());
        reservation.setTotalPrice(totalPrice);
        reservation.setStatus(ReservationStatus.CONFIRMED);

        // salvataggio
        ReservationEntity savedReservation =
                reservationRepository.save(reservation);

        return toResponse(savedReservation);
    }

    // recupero tutte prenotazioni
    public List<ReservationResponse> getAllReservations() {
        return reservationRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

     // ricerca per id
    public ReservationResponse getReservationById(Long id) {

        ReservationEntity reservation =
                reservationRepository.findById(id)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Prenotazione non trovata"
                        ));

        return toResponse(reservation);
    }
    
     // cancellazione prenotazione
    @Transactional
    public void cancelReservation(Long id) {
        ReservationEntity reservation = reservationRepository.findById(id)
                        .orElseThrow(() -> new ResponseStatusException( HttpStatus.NOT_FOUND, "Prenotazione non trovata"));

        if (reservation.getStatus() != ReservationStatus.CONFIRMED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Non è possibile cancellare questa prenotazione");
        }
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);
    }

    // check out
    @Transactional
    public void checkIn(Long id) {
        ReservationEntity reservation = reservationRepository.findById(id)
                        .orElseThrow(() -> new ResponseStatusException( HttpStatus.NOT_FOUND, "Prenotazione non trovata"));

        if (reservation.getStatus() != ReservationStatus.CONFIRMED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Impossibile effettuare il check-in");
        }

        // aggiornamento stato prenotazione
        reservation.setStatus(ReservationStatus.CHECKED_IN);

        // aggiornamento stato camera
        RoomEntity room = reservation.getRoom();
        room.setStatus(RoomStatus.OCCUPIED);
        roomRepository.save(room);
        reservationRepository.save(reservation);
    }


    // check-in
    @Transactional
    public void checkOut(Long id) {
        ReservationEntity reservation =  reservationRepository.findById(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prenotazione non trovata"));

        if (reservation.getStatus() != ReservationStatus.CHECKED_IN) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,  "Impossibile effettuare il check-out senza check-in");
        }

        // aggiornamento prenotazione
        reservation.setStatus(ReservationStatus.COMPLETED);

        // liberazione camera
        RoomEntity room = reservation.getRoom();
        if (room.getStatus() != RoomStatus.MAINTENANCE) {
            room.setStatus(RoomStatus.AVAILABLE);
        }

        roomRepository.save(room);
        reservationRepository.save(reservation);
    }

    // entity->dto
    private ReservationResponse toResponse(ReservationEntity reservation) {

        return new ReservationResponse(
                reservation.getId(),
                reservation.getCheckIn(),
                reservation.getCheckOut(),
                reservation.getGuests(),
                reservation.getTotalPrice(),
                reservation.getStatus(),
                reservation.getCustomer().getId(),
                reservation.getRoom().getId()
        );
    }


 }


