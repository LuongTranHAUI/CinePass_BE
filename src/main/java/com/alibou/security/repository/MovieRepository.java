package com.alibou.security.repository;

import com.alibou.security.entity.Movie;
import jakarta.persistence.Entity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    boolean existsByTitle(String title);

    @Query("SELECT m FROM Movie m LEFT JOIN FETCH m.showtimes WHERE m.id = :id")
    Optional<Movie> findById(@Param("id") Long id);
}
