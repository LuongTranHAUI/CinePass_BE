package com.alibou.security.repository;

import com.alibou.security.entity.Theater;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TheaterRepository extends JpaRepository<Theater, Integer> {
    Optional<Theater> findByName(String name);
    Optional<Theater> findById(Long id);
}
