package com.hotelmanagement.dto;

public record CustomerResponse(
    Long id,
    String firstName,
    String lastName,
    String email,
    String phone
) {}