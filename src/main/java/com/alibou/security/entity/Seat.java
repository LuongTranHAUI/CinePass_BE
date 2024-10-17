package com.alibou.security.entity;

import com.alibou.security.enums.SeatStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "seats", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"seat_row", "seat_number", "hall_id"})
})
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "seat_row")
    private Character seatRow;

    @Column(name = "seat_number")
    private Integer seatNumber;

    @Enumerated(EnumType.STRING)
    private SeatStatus status;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", updatable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "created_by", updatable = false)
    private Long createdBy;

    @Column(name = "updated_by", updatable = false)
    private Long updatedBy;

    @ManyToOne
    @JoinColumn(name = "hall_id")
    private Hall hall;
}
