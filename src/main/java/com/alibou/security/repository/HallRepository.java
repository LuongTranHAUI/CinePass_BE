package com.alibou.security.repository;

import com.alibou.security.entity.Hall;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HallRepository extends JpaRepository<Hall, Long> {
    Optional<Hall> findByName(String name);
    List<Hall> findByTheaterId(Long theaterId);
}
