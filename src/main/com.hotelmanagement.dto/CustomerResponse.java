package com.hotelmanagement.dto.customer;

public record CustomerResponse(
    Long id,
    String firstName,
    String lastName,
    String email,
    String phone
) {}