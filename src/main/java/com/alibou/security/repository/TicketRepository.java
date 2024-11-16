package com.alibou.security.repository;

import com.alibou.security.entity.Showtime;
import com.alibou.security.entity.Ticket;
import com.alibou.security.model.response.TicketResponse;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.repository.EntityGraph;
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
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    boolean existsById(Long id);
    void deleteByShowtimeId(Long showtimeId);

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM Ticket t WHERE t.showtime.id = :showtimeId AND t.seatNumber = :seatNumber")
    boolean existsByShowtimeIdAndSeatNumber(@Param("showtimeId") Long showtimeId, @Param("seatNumber") String seatNumber);

    @Query("SELECT new com.alibou.security.model.response.TicketResponse(t.id, t.seatNumber, t.ticketType, t.price, " +
            "t.serviceFee, t.status, t.createdAt, t.updatedAt, t.createdBy, t.updatedBy, " +
            "s.showTime, m.title, th.name, h.name) " +
            "FROM Ticket t " +
            "LEFT JOIN t.showtime s " +
            "LEFT JOIN s.movie m " +
            "LEFT JOIN s.theater th " +
            "LEFT JOIN s.hall h " +
            "WHERE t.user.id = :userId")
    List<TicketResponse> findAllByUserId(@Param("userId") Long userId);

    @Query("SELECT new com.alibou.security.model.response.TicketResponse(t.id, t.seatNumber, t.ticketType, t.price, " +
            "t.serviceFee, t.status, t.createdAt, t.updatedAt, t.createdBy, t.updatedBy, " +
            "s.showTime, m.title, th.name, h.name) " +
            "FROM Ticket t " +
            "LEFT JOIN t.showtime s " +
            "LEFT JOIN s.movie m " +
            "LEFT JOIN s.theater th " +
            "LEFT JOIN s.hall h ")
    List<TicketResponse> findAllTickets();

    @Modifying
    @Query("UPDATE Ticket t set t.showtime = null WHERE t.showtime.id IN (SELECT s.id FROM Showtime s WHERE s.movie.id = :movieId)")
    void setShowtimeIdToNull(@Param("movieId") Long movieId);

    @Modifying
    @Transactional
    @Query("UPDATE Ticket t set t.showtime = null WHERE t.showtime.id IN (SELECT s.id FROM Showtime s WHERE  s.id IN :showtimeId)")
    void setShowtimeIdToNullInDeleteShowtime(@Param("showtimeId") List <Long> showtimeId);
}
