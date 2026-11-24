package com.hotelmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotelmanagement.entity.RoomEntity;
import com.hotelmanagement.enums.RoomStatus;

public interface RoomRepository extends JpaRepository<RoomEntity, Long> {

    boolean existsByNumber(String number);

    List<RoomEntity> findByStatus(RoomStatus status);

    List<RoomEntity> findByStatusNot(RoomStatus status);

    
}