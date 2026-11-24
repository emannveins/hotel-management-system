package com.hotelmanagement.dto;

import java.math.BigDecimal;

import com.hotelmanagement.enums.RoomType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateRoomRequest(
    @NotBlank String number,
    @NotNull RoomType type,
    @NotNull @Positive BigDecimal nightlyRate,
    @Min(1) int capacity
) {}