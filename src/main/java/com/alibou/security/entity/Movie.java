package com.alibou.security.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String genre;
    private String director;
    private String actor;

    @Column(name = "run_time")
    private Integer runTime; // in minutes

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(name = "trailer_url", columnDefinition = "LONGTEXT")
    private String trailerUrl;

    @Column(name = "poster_url", columnDefinition = "LONGTEXT")
    private String posterUrl;

    @Column(name = "thumbnail_url", columnDefinition = "LONGTEXT")
    private String thumbnailUrl;

    @Column(name = "release_date")
    private LocalDateTime releaseDate;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", updatable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "created_by", updatable = false)
    private Long createdBy;

    @Column(name = "updated_by", updatable = false)
    private Long updatedBy;

    private Double rating;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    @JsonIgnoreProperties("movie")
    @ToString.Exclude
    private Set<MovieReview> reviews;

    @OneToMany(mappedBy = "movie", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JsonManagedReference
    @JsonIgnoreProperties("movie")
    @ToString.Exclude
    private Set<Showtime> showtimes;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return id != null && id.equals(movie.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
