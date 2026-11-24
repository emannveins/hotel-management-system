package com.hotelmanagement.repository;

import com.hotelmanagement.entity.RoomEntity;
import com.hotelmanagement.enums.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<RoomEntity, Long> {

    boolean existsByNumber(String number);

    List<RoomEntity> findByStatus(RoomStatus status);
}