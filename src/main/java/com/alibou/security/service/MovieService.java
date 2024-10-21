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

        // Cập nhật các review liên quan nếu cần
        movie.getReviews().forEach(review -> {
            // Thực hiện các thay đổi đối với review dựa trên yêu cầu
            movieReviewRepository.save(review);
        });

        return movieMapper.toMovieResponse(movieRepository.save(movie));
    }

    public void deleteMovieById(long id) {
        // Tìm movie cần xóa
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ApplicationContextException("Movie not found"));
        movieRepository.deleteById(id);
        movieReviewRepository.deleteById(movie.getId());
    }

    public MovieResponse addMovie(MovieRequest request) {
        // Kiểm tra xem phim đã tồn tại hay chưa
        if (movieRepository.existsByTitle(request.getTitle())) {
            throw new ApplicationContextException("Movie already exists");
        }

        // Chuyển đổi yêu cầu thành đối tượng Movie
        Movie movie = movieMapper.toMovie(request);
        movie.setId(null); // Đảm bảo tạo mới, không ghi đè lên ID hiện có

        // Thiết lập thời gian tạo và cập nhật
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        movie.setCreatedAt(currentTime.toLocalDateTime());
        movie.setUpdatedAt(currentTime.toLocalDateTime());

        try {
            // Lưu Movie đầu tiên để có ID cho việc liên kết
            Movie savedMovie = movieRepository.save(movie);

            // Tạo các đối tượng MovieReview từ yêu cầu và liên kết với Movie đã lưu
            HashSet<MovieReview> movieReviews = new HashSet<>();
            if (request.getReviews() != null) {
                request.getReviews().forEach(reviewRequest -> {
                    MovieReview movieReview = movieReviewMapper.toEntity(reviewRequest);
                    movieReview.setMovie(savedMovie); // Thiết lập liên kết với movie đã lưu
                    movieReview.setCreatedAt(currentTime.toLocalDateTime());
                    movieReview.setUpdatedAt(currentTime.toLocalDateTime());
                    movieReviews.add(movieReview);
                });
            }

            // Lưu tất cả các review đã liên kết
            movieReviewRepository.saveAll(movieReviews);

            // Cập nhật danh sách review vào Movie đã lưu
            savedMovie.setReviews(movieReviews);

            // Cập nhật danh sách showtimes
            HashSet<Showtime> showtimes = new HashSet<>();
            showTimeRepository.findById(savedMovie.getId()).ifPresent(showtimes::add);
            savedMovie.setShowtimes(showtimes);

            // Lưu lại Movie sau khi đã liên kết các review (nếu có thay đổi)
            movieRepository.save(savedMovie);

            // Trả về MovieResponse
            return movieMapper.toMovieResponse(savedMovie);

        } catch (Exception e) {
            log.error("Movie not added. Error: {}", e.getMessage(), e);
            throw new ApplicationContextException("Movie not added: " + e.getMessage());
        }
    }

}
