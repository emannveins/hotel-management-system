package com.hotelmanagement.dto.reservation;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Min;
import java.time.LocalDate;

public record CreateReservationRequest(
    @NotNull @Positive Long customerId,
    @NotNull @Positive Long roomId,
    @NotNull LocalDate checkIn,
    @NotNull LocalDate checkOut,
    @Min(1) int guests
) {}