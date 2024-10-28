package com.alibou.security.repository;

import com.alibou.security.entity.Ticket;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    boolean existsById(Long id);

    List<Ticket> findAllByUserId(Long userId);
}
