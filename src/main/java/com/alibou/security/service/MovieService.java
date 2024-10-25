package com.alibou.security.service;

import com.alibou.security.entity.Movie;
import com.alibou.security.entity.MovieReview;
import com.alibou.security.entity.Showtime;
import com.alibou.security.mapper.MovieMapper;
import com.alibou.security.mapper.MovieReviewMapper;
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
import java.util.Optional;

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

    @Autowired
    MovieReviewMapper movieReviewMapper;
    @Autowired
    private UserService userService;

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public MovieResponse getMovieById(long id) {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new ApplicationContextException("Movies not found"));

        double totalRating = movieReviewRepository.sumRatingByMovieId(id);
        int numberOfReviews = movieReviewRepository.countReviewsByMovieId(id);

        double averageRating = numberOfReviews > 0 ? totalRating / numberOfReviews : 0.0;

        movie.setRating(averageRating);
        Movie savedMovie = movieRepository.save(movie);

        movieMapper.toMovieResponse(savedMovie);
        return movieMapper.toMovieResponse(movie);
    }

    public MovieResponse updateMovieById(long id, MovieRequest request) {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new ApplicationContextException("Movies not found"));
        movieMapper.updateMovie(movie, request);

        movie.setCreatedAt(movie.getCreatedAt());
        movie.setUpdatedAt(new Timestamp(System.currentTimeMillis()).toLocalDateTime());
        movie.setUpdatedBy(userService.getCurrentUserId());
        // Cập nhật các review liên quan nếu cần
        movie.getReviews().forEach(review -> {
            // Thực hiện các thay đổi đối với review dựa trên yêu cầu
            movieReviewRepository.save(review);
        });

        return movieMapper.toMovieResponse(movieRepository.save(movie));
    }

    public void deleteMovieById(long id) {

        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ApplicationContextException("Movie not found"));
        movieRepository.deleteById(id);
        movieReviewRepository.deleteById(movie.getId());
    }

    public MovieResponse addMovie(MovieRequest request) {

        if (movieRepository.existsByTitle(request.getTitle())) {
            throw new ApplicationContextException("Movie already exists");
        }

        Movie movie = movieMapper.toMovie(request);
//        movie.setId(null);
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        movie.setCreatedAt(currentTime.toLocalDateTime());
        movie.setUpdatedAt(currentTime.toLocalDateTime());
        movie.setCreatedBy(userService.getCurrentUserId());

        try {
            Movie savedMovie = movieRepository.save(movie);

            return movieMapper.toMovieResponse(savedMovie);

        } catch (Exception e) {
            log.error("Movie not added. Error: {}", e.getMessage(), e);
            throw new ApplicationContextException("Movie not added: " + e.getMessage());
        }
    }

}
