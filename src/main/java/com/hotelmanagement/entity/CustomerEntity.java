
package com.hotelmanagement.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "customer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(length = 20)
    private String phone;

    @OneToMany(mappedBy = "customer")
    private List<ReservationEntity> reservations;
}
