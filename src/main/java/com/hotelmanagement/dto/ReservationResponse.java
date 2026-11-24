package com.hotelmanagement.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

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