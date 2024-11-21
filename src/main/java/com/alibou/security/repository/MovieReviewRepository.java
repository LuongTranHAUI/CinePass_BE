package com.alibou.security.repository;

import com.alibou.security.entity.MovieReview;
import com.alibou.security.model.response.MovieReviewResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MovieReviewRepository extends JpaRepository<MovieReview, Long> {
    Optional<MovieReview> findByMovieId(Long movieId);
    @Query("SELECT COALESCE(SUM(r.rating), 0) FROM MovieReview r WHERE r.movie.id = :movieId")
    double sumRatingByMovieId(@Param("movieId") Long movieId);

    @Query("SELECT new com.alibou.security.model.response.MovieReviewResponse(m.id,m.content, m.createdAt, m.createdBy, " +
            "m.rating, m.updatedAt, m.updatedBy, " +
            "s.title, u.username) " +
            "FROM MovieReview m " +
            "LEFT JOIN m.movie s " +
            "LEFT JOIN m.user u ")
    List<MovieReviewResponse> findAllReviews();

    @Query("SELECT COUNT(r) FROM MovieReview r WHERE r.movie.id = :movieId")
    int countReviewsByMovieId(@Param("movieId") Long movieId);

    @Query("SELECT s FROM MovieReview s WHERE s.movie.id = :movieId")
    List<MovieReview> findMovieReviewByMovieId(@Param("movieId") Long movieId);

    void deleteByMovieId(Long movieId);
}
