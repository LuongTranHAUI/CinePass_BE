package com.alibou.security.repository;

import com.alibou.security.entity.Hall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HallRepository extends JpaRepository<Hall, Long> {
    Optional<Hall> findByName(String name);
    Optional<Hall> findById(Long id);
}
