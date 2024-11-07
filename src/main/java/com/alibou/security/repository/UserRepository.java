package com.alibou.security.repository;

import com.alibou.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findById(Long id);
    Optional<User> findByUsernameOrEmail(String username, String email);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.status = false WHERE u.id = :id")
    void deactivateUserById(Long id);
}
