//package com.alibou.security.mapper.Impl;
//
//
//import com.alibou.security.entity.Movie;
//import com.alibou.security.entity.MovieReview;
//import com.alibou.security.entity.Showtime;
//import com.alibou.security.mapper.MovieMapper;
//import com.alibou.security.model.request.MovieRequest;
//import com.alibou.security.model.response.MovieResponse;
//import com.alibou.security.model.response.MovieReviewResponse;
//import com.alibou.security.model.response.ShowtimeResponse;
//
//import java.util.stream.Collectors;
//
//public class MovieMapperImpl implements MovieMapper {
//
//    @Override
//    public Movie toMovie(MovieRequest request) {
//        return null;
//    }
//
//    @Override
//    public MovieResponse toMovieResponse(Movie movie) {
//        if (movie == null) {
//            return null;
//        }
//
//        // Tạo một đối tượng MovieResponse mới
//        MovieResponse response = new MovieResponse();
//
//        // Ánh xạ các trường cơ bản từ Movie sang MovieResponse
//        response.setTitle(movie.getTitle());
//        response.setGenre(movie.getGenre());
//        response.setDuration(movie.getDuration());
//        response.setRating(movie.getRating());
//        response.setReleaseDate(movie.getReleaseDate());
//        response.setPosterUrl(movie.getPosterUrl());
//        response.setSummary(movie.getSummary());
//        response.setTrailerUrl(movie.getTrailerUrl());
//        response.setCreatedAt(movie.getCreatedAt());
//        response.setUpdatedAt(movie.getUpdatedAt());
//        response.setCreatedBy(movie.getCreatedBy());
//        response.setUpdatedBy(movie.getUpdatedBy());
//
//        // Ánh xạ các showtime và review
//        response.setShowtimes(
//                movie.getShowtimes().stream()
//                        .map(this::toShowtimeResponse) // Sử dụng phương thức ánh xạ thủ công cho Showtime
//                        .collect(Collectors.toSet())
//        );
//
//        response.setReviews(
//                movie.getReviews().stream()
//                        .map(this::toMovieReviewResponse) // Sử dụng phương thức ánh xạ thủ công cho MovieReview
//                        .collect(Collectors.toSet())
//        );
//
//        return response;
//    }
//
//    @Override
//    public void updateMovie(Movie movies, MovieRequest request) {
//
//    }
//
//    // Phương thức ánh xạ thủ công từ Showtime sang ShowtimeResponse
//    private ShowtimeResponse toShowtimeResponse(Showtime showtime) {
//        if (showtime == null) {
//            return null;
//        }
//
//        ShowtimeResponse response = new ShowtimeResponse();
//        response.setShowTime(showtime.getShowTime());
//        // Thêm các thuộc tính khác vào ShowtimeResponse nếu cần
//
//        return response;
//    }
//
//    // Phương thức ánh xạ thủ công từ MovieReview sang MovieReviewResponse
//    private MovieReviewResponse toMovieReviewResponse(MovieReview review) {
//        if (review == null) {
//            return null;
//        }
//
//        MovieReviewResponse response = new MovieReviewResponse();
//        response.setContent(review.getContent());
//        response.setRating(review.getRating());
//        response.setCreatedAt(review.getCreatedAt());
//        response.setUpdatedAt(review.getUpdatedAt());
//        // Thêm các thuộc tính khác vào MovieReviewResponse nếu cần
//
//        return response;
//    }
//}
