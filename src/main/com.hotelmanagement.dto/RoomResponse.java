package com.hotelmanagement.dto.room;

import java.math.BigDecimal;

public record RoomResponse(
    Long id,
    String number,
    RoomType type,
    BigDecimal nightlyRate,
    int capacity,
    String status
) {}