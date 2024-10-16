package com.alibou.security.entity;

import com.alibou.security.enums.HallStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "halls")
public class Hall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "theater_id")
    private Theater theater;

    private String name;

    @Column(name = "seat_capacity")
    private Integer seatCapacity;

    @Enumerated(EnumType.STRING)
    private HallStatus status;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", updatable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "created_by", updatable = false)
    private Long createdBy;

    @Column(name = "updated_by", updatable = false)
    private Long updatedBy;

    @OneToMany(mappedBy = "hall")
    private Set<Showtime> showtimes;

    @Column(name = "max_capacity")
    private Integer maxCapacity;

    @Column(columnDefinition = "TEXT")
    private String seatingPlanJson; // Thêm trường JSON
}
