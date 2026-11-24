package com.hotelmanagement.dto;

import java.math.BigDecimal;

import com.hotelmanagement.enums.RoomType;

public record RoomResponse(
    Long id,
    String number,
    RoomType type,
    BigDecimal nightlyRate,
    int capacity,
    String status
) {}