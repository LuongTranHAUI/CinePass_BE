package com.alibou.security.service;

import com.alibou.security.entity.Movie;
import com.alibou.security.entity.MovieReview;
import com.alibou.security.entity.Showtime;
import com.alibou.security.mapper.MovieMapper;
import com.alibou.security.mapper.MovieReviewMapper;
import com.alibou.security.model.request.MovieRequest;
import com.alibou.security.model.response.MovieResponse;
import com.alibou.security.model.response.MovieReviewResponse;
import com.alibou.security.model.response.ShowtimeResponse;
import com.alibou.security.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.MulticastChannel;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
    @Autowired
    private DiscountApplicationRepository discountApplicationRepository;
    @Autowired
    private TicketRepository ticketRepository;

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

        List<LocalDateTime> showtimes = showTimeRepository.findShowtimesByMovieId(id);

        MovieResponse response = movieMapper.toMovieResponse(movie);

        // Chuyển đổi LocalDateTime thành ShowtimeResponse
        Set<ShowtimeResponse> showtimeResponses = showtimes.stream()
                .map(showTime -> {
                    ShowtimeResponse showtimeResponse = new ShowtimeResponse();
                    showtimeResponse.setShowTime(showTime);
                    return showtimeResponse;
                })
                .collect(Collectors.toSet());

        response.setShowtimes(showtimeResponses);

        List<?> movieReviews = movieReviewRepository.findMovieReviewByMovieId(id);
        Set<MovieReviewResponse> movieReviewResponses = movieReviews.stream().map(movieReview -> {
            MovieReviewResponse movieReviewResponse = new MovieReviewResponse();
            movieReviewResponse.setContent((String) movieReview);
            return movieReviewResponse;
        }).collect(Collectors.toSet());
        response.setReviews(movieReviewResponses);

        return response;
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

    @Transactional
    public void deleteMovieById(long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ApplicationContextException("Movie not found"));


        // Delete associated showtimes
        ticketRepository.setShowtimeIdToNull(movie.getId());
        showTimeRepository.setMovieIdToNull(movie.getId());
        movieReviewRepository.deleteByMovieId(movie.getId());
        // Delete movie
        movieRepository.deleteById(id);
    }

    public String encodeImageToBase64(MultipartFile image) throws IOException {

        byte[] bytes = image.getBytes();

        String encodedImage = Base64.getEncoder().encodeToString(bytes);
        return encodedImage;
    }

    public MovieResponse addMovie(MovieRequest request) throws IOException {

        if (movieRepository.existsByTitle(request.getTitle())) {
            throw new ApplicationContextException("Movie already exists");
        }

        Movie movie = movieMapper.toMovie(request);
//        movie.setId(null);
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        movie.setCreatedAt(currentTime.toLocalDateTime());
        movie.setUpdatedAt(currentTime.toLocalDateTime());
        movie.setCreatedBy(userService.getCurrentUserId());

        String imagePath = request.getPosterUrl();
//        String base64Image = encodeImageToBase64(imagePath);
        movie.setPosterUrl(imagePath);

        String thumbnailPath = request.getThumbnailUrl();
        movie.setThumbnailUrl(thumbnailPath);

        String trailerPath = request.getTrailerUrl();
        movie.setTrailerUrl(trailerPath);

        try {
            Movie savedMovie = movieRepository.save(movie);

            return movieMapper.toMovieResponse(savedMovie);

        } catch (Exception e) {
            log.error("Movie not added. Error: {}", e.getMessage(), e);
            throw new ApplicationContextException("Movie not added: " + e.getMessage());
        }
    }

}
