package com.alibou.security.service;

import com.alibou.security.entity.Movie;
import com.alibou.security.entity.MovieReview;
import com.alibou.security.entity.Showtime;
import com.alibou.security.mapper.MovieMapper;
import com.alibou.security.model.request.MovieRequest;
import com.alibou.security.model.response.MovieResponse;
import com.alibou.security.repository.MovieRepository;
import com.alibou.security.repository.MovieReviewRepository;
import com.alibou.security.repository.ShowTimeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;

@Service
@Slf4j
public class MovieService {
    @Autowired
    MovieRepository movieRepository;

    @Autowired
    MovieReviewRepository movieReviewRepository;

    @Autowired
    ShowTimeRepository showTimeRepository;

    @Autowired
    MovieMapper movieMapper;

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public MovieResponse getMovieById(long id) {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new ApplicationContextException("Movies not found"));
        return movieMapper.toMovieResponse(movie);
    }

    public MovieResponse updateMovieById(long id, MovieRequest request) {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new ApplicationContextException("Movies not found"));
        movieMapper.updateMovie(movie, request);

        movie.setCreatedAt(movie.getCreatedAt());
        movie.setUpdatedAt(new Timestamp(System.currentTimeMillis()).toLocalDateTime());

        return movieMapper.toMovieResponse(movieRepository.save(movie));
    }

    public void deleteMovieById(long id) {
        movieRepository.deleteById(id);
    }

    public MovieResponse addMovie(MovieRequest request) {
        if(movieRepository.existsByTitle(request.getTitle())) {
            throw new ApplicationContextException("Movie already exists");
        }

        Movie movie = movieMapper.toMovie(request);
        movie.setId(null);

        movie.setCreatedAt(new Timestamp(System.currentTimeMillis()).toLocalDateTime());
        movie.setUpdatedAt(new Timestamp(System.currentTimeMillis()).toLocalDateTime());

//        movie.setId(null);


        try {
            movie = movieRepository.save(movie);

            HashSet<MovieReview> movieReviews = new HashSet<>();
            HashSet<Showtime> showtimes = new HashSet<>();

            movieReviewRepository.findByMovieId(movie.getId()).ifPresent(movieReviews::add );
            showTimeRepository.findById(movie.getId()).ifPresent(showtimes::add);

            movie.setReviews(movieReviews);
            movie.setShowtimes(showtimes);

            movie = movieRepository.save(movie);


        } catch (Exception e) {
            log.error("Movie not added. Error: {}", e.getMessage(), e);
            throw new ApplicationContextException("Movie not added: " + e.getMessage());
        }

        return movieMapper.toMovieResponse(movie);
    }
}
