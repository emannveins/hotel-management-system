package com.hotelmanagement.dto.reservation;

import java.time.LocalDate;
import java.math.BigDecimal;

public record ReservationResponse(
    Long id,
    Long customerId,
    Long roomId,
    String roomNumber,
    LocalDate checkIn,
    LocalDate checkOut,
    int guests,
    BigDecimal totalPrice,
    String status
) {}