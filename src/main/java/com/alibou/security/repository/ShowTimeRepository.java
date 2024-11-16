package com.alibou.security.repository;

import com.alibou.security.entity.Showtime;
import com.alibou.security.model.response.interfaces.ShowtimeResponseInterface;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShowTimeRepository extends JpaRepository<Showtime, Long> {
    Optional<Showtime> findById(Long id);
    boolean existsByHallId(long id);
    boolean existsByShowTime(LocalDateTime showtime);
    boolean existsByTheaterId(long id);
    void deleteByMovieId(Long movieId);

    @Query("SELECT s.showTime AS showTime,s.id AS id, s.movie.title AS movieTitle, s.theater.name AS theaterName, s.hall.name AS hallName FROM Showtime s")
    List<ShowtimeResponseInterface> findAllShowTimes();

    @Query("SELECT s FROM Showtime s WHERE s.movie.id = :movieId")
    List<Showtime> findShowtimesByMovieId(@Param("movieId") Long movieId);

    @Modifying
    @Query("UPDATE Showtime s SET s.movie = null WHERE s.movie.id = :movieId")
    void setMovieIdToNull(@Param("movieId") Long movieId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Showtime s WHERE s.showTime < :dateTime")
    void deleteByShowTimeBefore(@Param("dateTime") LocalDateTime dateTime);

    @Query("SELECT s.id FROM Showtime  s WHERE s.showTime < :dateTime")
    List<Long> getShowTimeId(@Param("dateTime") LocalDateTime dateTime);
//    @Modifying
//    @Query("UPDATE Showtime s SET s.movie = null WHERE s.movie.id = :movieId")
//    void setMovieIdToNull(@Param("movieId") Long movieId);
}
