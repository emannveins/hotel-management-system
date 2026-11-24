package com.hotelmanagement.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotelmanagement.entity.ReservationEntity;
import com.hotelmanagement.enums.ReservationStatus;

public interface ReservationRepository
        extends JpaRepository<ReservationEntity, Long> {

    // Controlla se esiste una prenotazione sovrapposta
    boolean existsByRoom_IdAndStatusNotAndCheckInBeforeAndCheckOutAfter(
            Long roomId,
            ReservationStatus status,
            LocalDate checkIn,
            LocalDate checkOut
    );

    // Recupera tutte le prenotazioni sovrapposte
    List<ReservationEntity> findByStatusNotAndCheckInBeforeAndCheckOutAfter(
            ReservationStatus status,
            LocalDate checkOut,
            LocalDate checkIn
    );
}