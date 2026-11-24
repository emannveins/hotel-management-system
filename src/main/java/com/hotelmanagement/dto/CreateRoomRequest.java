package com.hotelmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;

public record CreateRoomRequest(
    @NotBlank String number,
    @NotNull RoomType type,
    @NotNull @Positive BigDecimal nightlyRate,
    @Min(1) int capacity
) {}