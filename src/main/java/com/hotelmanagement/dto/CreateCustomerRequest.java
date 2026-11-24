package com.hotelmanagement.dto;


public record CreateCustomerRequest(
    @NotBlank String firstName,
    @NotBlank String lastName,
     String email,
    @NotBlank String phone
) {

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}