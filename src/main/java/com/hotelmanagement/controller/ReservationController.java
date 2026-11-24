package com.hotelmanagement.controller;

import com.hotelmanagement.dto.reservation.*;
import com.hotelmanagement.service.ReservationService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/all")
    public List<ReservationResponse> getAllReservations() {
        return reservationService.getAllReservations();
    }

    @GetMapping("/{id}")
    public ReservationResponse getReservationById(
            @PathVariable Long id) {
        return reservationService.getReservationById(id);
    }

    @PatchMapping("/{id}/cancel")
    public ReservationResponse cancelReservation(
            @PathVariable Long id) {
        return reservationService.cancelReservation(id);
    }

    @PatchMapping("/{id}/check-in")
    public ReservationResponse checkIn(
            @PathVariable Long id) {
        return reservationService.checkIn(id);
    }

    @PatchMapping("/{id}/check-out")
    public ReservationResponse checkOut(
            @PathVariable Long id) {
        return reservationService.checkOut(id);
    }

     @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponse createReservation(
            @Valid @RequestBody CreateReservationRequest request) {
        return reservationService.createReservation(request);
    }
}