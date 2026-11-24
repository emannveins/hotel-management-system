
package com.hotelmanagement.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotelmanagement.entity.ReservationEntity;
import com.hotelmanagement.enums.ReservationStatus;

public interface ReservationRepository
        extends JpaRepository<ReservationEntity, Long> {

    boolean existsByRoom_IdAndStatusNotAndCheckInBeforeAndCheckOutAfter(
            Long roomId,
            ReservationStatus status,
            LocalDate checkIn,
            LocalDate checkOut
    );
}

