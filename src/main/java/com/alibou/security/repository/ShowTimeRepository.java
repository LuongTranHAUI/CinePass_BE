package com.alibou.security.repository;

import com.alibou.security.entity.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ShowTimeRepository extends JpaRepository<Showtime, Long> {
    Optional<Showtime> findById(Long id);
    boolean existsByHallId(long id);
    boolean existsByShowTime(LocalDateTime showtime);
}
