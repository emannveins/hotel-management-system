
package com.hotelmanagement.entity;

import jakarta.persistence.*;
import lombok.*;
import com.hotelmanagement.enums.RoomStatus;
import com.hotelmanagement.enums.RoomType;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "room")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String number;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private RoomType type;

    @Column(name = "nightly_rate", nullable = false,
            precision = 10, scale = 2)
    private BigDecimal nightlyRate;

    @Column(nullable = false)
    private Integer capacity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private RoomStatus status;

    @OneToMany(mappedBy = "room")
    private List<ReservationEntity> reservations;
}
