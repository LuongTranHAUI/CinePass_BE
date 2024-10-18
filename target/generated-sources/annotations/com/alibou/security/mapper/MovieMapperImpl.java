package com.alibou.security.mapper;

import com.alibou.security.entity.Movie;
import com.alibou.security.entity.MovieReview;
import com.alibou.security.entity.Showtime;
import com.alibou.security.model.request.MovieRequest;
import com.alibou.security.model.response.MovieResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.9 (Oracle Corporation)"
)
@Component
public class MovieMapperImpl implements MovieMapper {

    @Override
    public Movie toMovie(MovieRequest request) {
        if ( request == null ) {
            return null;
        }

        Movie.MovieBuilder movie = Movie.builder();

        movie.title( request.getTitle() );
        movie.genre( request.getGenre() );
        movie.duration( request.getDuration() );
        movie.summary( request.getSummary() );
        movie.trailerUrl( request.getTrailerUrl() );
        movie.posterUrl( request.getPosterUrl() );
        if ( request.getReleaseDate() != null ) {
            movie.releaseDate( LocalDateTime.ofInstant( request.getReleaseDate().toInstant(), ZoneId.of( "UTC" ) ) );
        }
        movie.createdAt( request.getCreatedAt() );
        movie.updatedAt( request.getUpdatedAt() );
        movie.createdBy( request.getCreatedBy() );
        movie.updatedBy( request.getUpdatedBy() );
        movie.rating( request.getRating() );
        Set<MovieReview> set = request.getReviews();
        if ( set != null ) {
            movie.reviews( new LinkedHashSet<MovieReview>( set ) );
        }
        Set<Showtime> set1 = request.getShowtimes();
        if ( set1 != null ) {
            movie.showtimes( new LinkedHashSet<Showtime>( set1 ) );
        }

        return movie.build();
    }

    @Override
    public MovieResponse toMovieResponse(Movie movie) {
        if ( movie == null ) {
            return null;
        }

        MovieResponse.MovieResponseBuilder movieResponse = MovieResponse.builder();

        movieResponse.title( movie.getTitle() );
        if ( movie.getDuration() != null ) {
            movieResponse.duration( movie.getDuration() );
        }
        movieResponse.genre( movie.getGenre() );
        if ( movie.getReleaseDate() != null ) {
            movieResponse.releaseDate( Date.from( movie.getReleaseDate().toInstant( ZoneOffset.UTC ) ) );
        }
        movieResponse.posterUrl( movie.getPosterUrl() );
        movieResponse.summary( movie.getSummary() );
        movieResponse.trailerUrl( movie.getTrailerUrl() );
        movieResponse.createdAt( movie.getCreatedAt() );
        movieResponse.updatedAt( movie.getUpdatedAt() );
        movieResponse.createdBy( movie.getCreatedBy() );
        movieResponse.updatedBy( movie.getUpdatedBy() );
        movieResponse.rating( movie.getRating() );
        Set<MovieReview> set = movie.getReviews();
        if ( set != null ) {
            movieResponse.reviews( new LinkedHashSet<MovieReview>( set ) );
        }
        Set<Showtime> set1 = movie.getShowtimes();
        if ( set1 != null ) {
            movieResponse.showtimes( new LinkedHashSet<Showtime>( set1 ) );
        }

        return movieResponse.build();
    }

    @Override
    public void updateMovie(Movie movies, MovieRequest request) {
        if ( request == null ) {
            return;
        }

        movies.setTitle( request.getTitle() );
        movies.setGenre( request.getGenre() );
        movies.setDuration( request.getDuration() );
        movies.setSummary( request.getSummary() );
        movies.setTrailerUrl( request.getTrailerUrl() );
        movies.setPosterUrl( request.getPosterUrl() );
        if ( request.getReleaseDate() != null ) {
            movies.setReleaseDate( LocalDateTime.ofInstant( request.getReleaseDate().toInstant(), ZoneId.of( "UTC" ) ) );
        }
        else {
            movies.setReleaseDate( null );
        }
        movies.setUpdatedAt( request.getUpdatedAt() );
        movies.setCreatedBy( request.getCreatedBy() );
        movies.setUpdatedBy( request.getUpdatedBy() );
        movies.setRating( request.getRating() );
        if ( movies.getReviews() != null ) {
            Set<MovieReview> set = request.getReviews();
            if ( set != null ) {
                movies.getReviews().clear();
                movies.getReviews().addAll( set );
            }
            else {
                movies.setReviews( null );
            }
        }
        else {
            Set<MovieReview> set = request.getReviews();
            if ( set != null ) {
                movies.setReviews( new LinkedHashSet<MovieReview>( set ) );
            }
        }
        if ( movies.getShowtimes() != null ) {
            Set<Showtime> set1 = request.getShowtimes();
            if ( set1 != null ) {
                movies.getShowtimes().clear();
                movies.getShowtimes().addAll( set1 );
            }
            else {
                movies.setShowtimes( null );
            }
        }
        else {
            Set<Showtime> set1 = request.getShowtimes();
            if ( set1 != null ) {
                movies.setShowtimes( new LinkedHashSet<Showtime>( set1 ) );
            }
        }
    }
}
