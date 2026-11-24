package com.hotelmanagement.service;

import com.hotelmanagement.dto.room.CreateRoomRequest;
import com.hotelmanagement.dto.room.RoomResponse;
import com.hotelmanagement.dto.room.RoomType;
import com.hotelmanagement.entity.RoomEntity;
import com.hotelmanagement.enums.RoomStatus;
import com.hotelmanagement.enums.ReservationStatus;
import com.hotelmanagement.repository.RoomRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class RoomService{
    private final Room roomRepository;

    public RoomService(RoomRepository roomRepository){
        this.roomRepository = roomRepository;
    }
    // trova tutte  le camere
}