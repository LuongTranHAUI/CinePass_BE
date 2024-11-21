package com.alibou.security.api.user;

import com.alibou.security.entity.MovieReview;
import com.alibou.security.model.request.MovieReviewRequest;
import com.alibou.security.model.response.MovieReviewResponse;
import com.alibou.security.service.MovieReviewService;
import com.alibou.security.service.TicketService;
import com.alibou.security.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/reviews")
@Slf4j
public class MovieReviewAPI {
    private final MovieReviewService movieReviewService;
    private final Logger logger = LoggerFactory.getLogger(MovieReviewAPI.class);
    private final UserService userService;
    private final TicketService ticketService;

    @GetMapping
    public ResponseEntity<List<MovieReviewResponse>> getMovieReviews() {
        try {
            List<MovieReviewResponse> movieReviews = movieReviewService.findAllMovieReviews();
            return ResponseEntity.ok(movieReviews);
        } catch (Exception e) {
            logger.error("Error show all comment.", e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMovieReviewById(@PathVariable("id") long id) {
        try {
            MovieReviewResponse movieReviewResponse = movieReviewService.findById(id);
            logger.info(movieReviewResponse.toString());
            return ResponseEntity.status(200).body(movieReviewResponse);
        } catch (Exception e) {
            logger.error("Error show comment.", e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMovieReview(@PathVariable("id") long id, @RequestBody MovieReviewRequest movieReviewRequest) {

        try {
            MovieReviewResponse movieReviewResponse = movieReviewService.updateMovieReview(id, movieReviewRequest);
            logger.info(movieReviewResponse.toString());
            return ResponseEntity.status(200).body(movieReviewResponse);
        } catch (Exception e) {
            logger.error("Error update comment.", e.getMessage());
            return ResponseEntity.status(500).body(null);
        }

    }

    @PostMapping
    public ResponseEntity<?> createMovieReview(@RequestBody MovieReviewRequest movieReviewRequest) {
        try {
            MovieReviewResponse movieReviewResponse = movieReviewService.createMovieReview(movieReviewRequest);
            logger.info(movieReviewResponse.toString());
            return ResponseEntity.status(200).body(movieReviewResponse);
        } catch (Exception e) {
            logger.error("Error creating movie review: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("An error occurred while creating the movie review.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMovieReview(@PathVariable long id) {
        try {
            movieReviewService.deleteMovieReview(id);
            logger.info("Delete MovieReview with id {}", id);
            return ResponseEntity.status(204).body(null);
        } catch (Exception e) {
            logger.error("Error delete comment.", e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }
}
