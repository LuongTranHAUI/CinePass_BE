package com.alibou.security.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name = "showtimes")
public class Showtime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    @JsonBackReference
    @JsonIgnore
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "theater_id")
    @JsonBackReference
    @JsonIgnore
    private Theater theater;

    @ManyToOne
    @JoinColumn(name = "hall_id")
    @JsonBackReference
    @JsonIgnore
    private Hall hall;

    @Column(name = "show_time", nullable = false)
    private LocalDateTime showTime;

    @OneToMany(mappedBy = "showtime")
    @JsonManagedReference
    @JsonIgnore
    private Set<Ticket> tickets;

}
