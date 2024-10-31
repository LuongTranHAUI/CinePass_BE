package com.alibou.security.repository;

import com.alibou.security.entity.DiscountApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountApplicationRepository extends JpaRepository<DiscountApplication, Long> {
}
