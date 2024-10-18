package com.alibou.security.repository;

import com.alibou.security.entity.MovieReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieReviewRepository extends JpaRepository<MovieReview, Long> {
    Optional<MovieReview> findByMovieId(Long movieId);
}
