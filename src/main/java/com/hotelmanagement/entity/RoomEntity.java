
package com.hotelmanagement.entity;

import java.math.BigDecimal;
import java.util.List;

import com.hotelmanagement.enums.RoomStatus;
import com.hotelmanagement.enums.RoomType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    public BigDecimal getPricePerNight() {
        throw new UnsupportedOperationException("Unimplemented method 'getPricePerNight'");
    }
}
