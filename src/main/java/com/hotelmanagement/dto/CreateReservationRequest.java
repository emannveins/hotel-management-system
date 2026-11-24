package com.hotelmanagement.dto;

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
) {

    public Long getCustomerId() {
        return customerId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public int getGuests() {
        return guests;
    }
}