package com.hotelmanagement.controller;



import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.hotelmanagement.Service.RoomService;
import com.hotelmanagement.dto.CreateRoomRequest;
import com.hotelmanagement.dto.RoomResponse;
import com.hotelmanagement.enums.RoomType;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/all")
    public List<RoomResponse> getAllRooms() {
        return roomService.getAllRooms();
    }

    @GetMapping("/available")
    public List<RoomResponse> getAvailableRooms(
            @RequestParam LocalDate checkIn,
            @RequestParam LocalDate checkOut,
            @RequestParam(required = false) RoomType type) {

        return roomService.getAvailableRooms(
                checkIn, checkOut, type);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public RoomResponse createRoom(
            @Valid @RequestBody CreateRoomRequest request) {
        return roomService.createRoom(request);
    }
}