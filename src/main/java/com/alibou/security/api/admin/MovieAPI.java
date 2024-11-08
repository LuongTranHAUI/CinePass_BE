package com.alibou.security.api.admin;

import com.alibou.security.entity.Movie;
import com.alibou.security.mapper.MovieMapper;
import com.alibou.security.model.request.MovieRequest;
import com.alibou.security.model.response.MovieResponse;
import com.alibou.security.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/management/movies")
@RequiredArgsConstructor
public class MovieAPI {
    private static final Logger logger = LoggerFactory.getLogger(MovieAPI.class);
    private final MovieService service;

    @Autowired
    MovieService movieService;

    @Autowired
    MovieMapper movieMapper;

    @PostMapping
    public ResponseEntity<?> addMovie(@RequestPart("movieRequest") MovieRequest movieRequest,
                                      @RequestPart("posterUrl") MultipartFile image,
                                      @RequestPart("thumbnailUrl") MultipartFile thumbnailUrl,
                                      @RequestPart("trailerUrl") MultipartFile trailerUrl) {
        try {
            String base64Image = movieService.encodeImageToBase64(image);
            String base64Thumbnail = movieService.encodeImageToBase64(thumbnailUrl);
            String base64Trailer = movieService.encodeImageToBase64(trailerUrl);

            movieRequest.setPosterUrl(base64Image);
            movieRequest.setThumbnailUrl(base64Thumbnail);
            movieRequest.setTrailerUrl(base64Trailer);

            MovieResponse movieResponse = movieService.addMovie(movieRequest);
            logger.info("Movie added successfully: {}",movieRequest);
            return ResponseEntity.status(201).body(movieResponse);

        } catch (Throwable e) {
            logger.error("Error saving Movie: {}", e.getMessage());
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Movie>> getMovieList() {

        try {

                List<Movie> movies= movieService.getAllMovies();
                logger.info("Movie list retrieved successfully");
                return ResponseEntity.status(200).body(movies);

        } catch (Exception e) {
            logger.error("Error getting movies: {}", e.getMessage());
            return ResponseEntity.status(500).body(null);
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMovie(@PathVariable int id) {
        try {

            MovieResponse movieResponse = movieService.getMovieById(id);
            logger.info("Movie retrieved successfully: {}", movieResponse);
            return ResponseEntity.status(200).body(movieResponse);

        } catch (Exception e) {
            logger.error("Error getting movie: {}", e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMovie(@PathVariable int id, @RequestBody MovieRequest movieRequest) {
        try {
            MovieResponse movieResponse = movieService.updateMovieById(id, movieRequest);
            logger.info("Movie updated successfully: {}", movieRequest);
            return ResponseEntity.status(200).body(movieResponse);

        } catch (Exception e) {
            logger.error("Error saving movie: {}", e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable int id) {
        try {
            movieService.deleteMovieById(id);
            logger.info("Movie deleted successfully: {}", id);
            return ResponseEntity.status(201).body(null);
        } catch (Exception e) {
            logger.error("Error deleting movie: {}", e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(Exception e) {
        logger.error("Unhandled exception: {}", e.getMessage());
        return ResponseEntity.status(500).body("Internal server error");
    }
}
