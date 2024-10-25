package com.alibou.security.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "theaters")
public class Theater {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    private String location;

    @Column(name = "phone")
    private String phone;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", updatable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "created_by", updatable = false)
    private Long createdBy;

    @Column(name = "updated_by", updatable = false)
    private Long updatedBy;

    @OneToMany(mappedBy = "theater")
    @JsonManagedReference
    private Set<Hall> halls;

    @OneToMany(mappedBy = "theater")
    @JsonBackReference
    private Set<Showtime> showtimes;
}
