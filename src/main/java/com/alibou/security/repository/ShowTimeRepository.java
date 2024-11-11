package com.alibou.security.repository;

import com.alibou.security.entity.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShowTimeRepository extends JpaRepository<Showtime, Long> {
    Optional<Showtime> findById(Long id);
    boolean existsByHallId(long id);
    boolean existsByShowTime(LocalDateTime showtime);
    void deleteByMovieId(Long movieId);

    @Query("SELECT s.showTime FROM Showtime s WHERE s.movie.id = :movieId")
    List<LocalDateTime> findShowtimesByMovieId(@Param("movieId") Long movieId);

    @Modifying
    @Query("UPDATE Showtime s SET s.movie = null WHERE s.movie.id = :movieId")
    void setMovieIdToNull(@Param("movieId") Long movieId);

//    @Modifying
//    @Query("UPDATE Showtime s SET s.movie = null WHERE s.movie.id = :movieId")
//    void setMovieIdToNull(@Param("movieId") Long movieId);
}
