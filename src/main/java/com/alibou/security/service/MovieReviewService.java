package com.alibou.security.service;

import com.alibou.security.entity.Movie;
import com.alibou.security.entity.MovieReview;
import com.alibou.security.entity.Ticket;
import com.alibou.security.entity.User;
import com.alibou.security.mapper.MovieReviewMapper;
import com.alibou.security.model.request.MovieReviewRequest;
import com.alibou.security.model.response.MovieReviewResponse;
import com.alibou.security.repository.MovieRepository;
import com.alibou.security.repository.MovieReviewRepository;
import com.alibou.security.repository.TicketRepository;
import com.alibou.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class MovieReviewService {
    @Autowired
    private MovieReviewRepository movieReviewRepository;
    @Autowired
    private MovieReviewMapper movieReviewMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private TicketRepository ticketRepository;

    public List<MovieReviewResponse> findAllMovieReviews() {
//        List<MovieReview> movieReviews = movieReviewRepository.findAll();
        return movieReviewRepository.findAllReviews();
    }

    public MovieReviewResponse findById(long id) {
        MovieReview movieReview = movieReviewRepository.findById(id).orElseThrow(() -> new ApplicationContextException("Movie Review Not Found"));

        return movieReviewMapper.toMovieReviewResponse(movieReview);
    }

    public MovieReviewResponse createMovieReview(MovieReviewRequest movieReviewRequest) {
        MovieReview movieReview = movieReviewMapper.toMovieReview(movieReviewRequest);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        movieReview.setCreatedAt(timestamp.toLocalDateTime());
        movieReview.setUpdatedAt(timestamp.toLocalDateTime());
        movieReview.setCreatedBy(userService.getCurrentUserId());
        movieReview.setUpdatedBy(userService.getCurrentUserId());

        User user = userRepository.findById(Math.toIntExact(movieReviewRequest.getUserId())).orElseThrow(() -> new ApplicationContextException("User Not Found"));

        Movie movie = movieRepository.findById(movieReviewRequest.getMovieId()).orElseThrow(() -> new ApplicationContextException("Movie Not Found"));

        movieReview.setUser(user);
        movieReview.setMovie(movie);

        try {
            movieReview = movieReviewRepository.save(movieReview);
            return movieReviewMapper.toMovieReviewResponse(movieReview);
        }catch (Exception e) {
            throw new ApplicationContextException("Error comment to movie.");
        }
    }

    public MovieReviewResponse updateMovieReview(long id , MovieReviewRequest movieReviewRequest) {
        MovieReview movieReview = movieReviewRepository.findById(id).orElseThrow(() -> new ApplicationContextException("Ticket Not Found"));
        movieReviewMapper.updateMovieReview(movieReview, movieReviewRequest);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        movieReview.setUpdatedAt(timestamp.toLocalDateTime());
        movieReview.setUpdatedBy(userService.getCurrentUserId());

        return movieReviewMapper.toMovieReviewResponse(movieReviewRepository.save(movieReview));
    }

    public void deleteMovieReview(Long id) {
        movieReviewRepository.deleteById(id);
    }
}
