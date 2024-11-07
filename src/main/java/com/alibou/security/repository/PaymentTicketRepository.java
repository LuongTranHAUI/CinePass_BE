package com.alibou.security.repository;

import com.alibou.security.entity.PaymentTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentTicketRepository extends JpaRepository<PaymentTicket, Long> {
}
