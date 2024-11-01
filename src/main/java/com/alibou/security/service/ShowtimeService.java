package com.alibou.security.service;

import com.alibou.security.entity.Hall;
import com.alibou.security.entity.Movie;
import com.alibou.security.entity.Showtime;
import com.alibou.security.entity.Theater;
import com.alibou.security.mapper.ShowtimeMapper;
import com.alibou.security.model.request.ShowtimeRequest;
import com.alibou.security.model.response.ShowtimeResponse;
import com.alibou.security.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ShowtimeService {
    @Autowired
    ShowtimeMapper showtimeMapper;

    @Autowired
    ShowTimeRepository showTimeRepository;

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    TheaterRepository theaterRepository;

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    HallRepository hallRepository;

    public List<Showtime> getAllShowtime(){ return showTimeRepository.findAll();}

    public ShowtimeResponse getShowtimeById(long id){
        Showtime showtime = showTimeRepository.findById(id).orElseThrow(() -> new ApplicationContextException("Don't find Show Time."));

        return showtimeMapper.toshowtimeResponse(showtime);
    }

    public ShowtimeResponse updateShowtime(long id, ShowtimeRequest request){
        Showtime showtime = showTimeRepository.findById(id).orElseThrow(() -> new ApplicationContextException("Don't find Show Time."));

        showtimeMapper.updateShowtime(showtime, request);

        return showtimeMapper.toshowtimeResponse(showTimeRepository.save(showtime));
    }

    @Transactional
    public void deleteShowtime(long id) {
        Showtime showtime = showTimeRepository.findById(id)
                .orElseThrow(() -> new ApplicationContextException("Showtime not found"));

        // Delete associated tickets
        ticketRepository.deleteByShowtimeId(showtime.getId());

        // Delete showtime
        showTimeRepository.deleteById(id);
    }

    public ShowtimeResponse addShowtime(ShowtimeRequest request){

        if (request == null || request.getHallId() == null || request.getMovieId() == null || request.getTheaterId() == null) {
            throw new ApplicationContextException("Hall, Movie, or Theater information is missing.");
        }

        // Tìm các đối tượng Hall, Movie, Theater từ cơ sở dữ liệu
        Hall hall = hallRepository.findById(request.getHallId())
                .orElseThrow(() -> new ApplicationContextException("Hall not found"));
        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new ApplicationContextException("Movie not found"));
        Theater theater = theaterRepository.findById(request.getTheaterId())
                .orElseThrow(() -> new ApplicationContextException("Theater not found"));


        if (showTimeRepository.existsByHallId(hall.getId()) && showTimeRepository.existsByShowTime(request.getShowTime())) {
           throw new ApplicationContextException("Had the movie") ;
        }

        Showtime showtime = showtimeMapper.toShowtime(request);
        showtime.setHall(hall);
        showtime.setMovie(movie);
        showtime.setTheater(theater);
        try {
             showtime = showTimeRepository.save(showtime);

             return showtimeMapper.toshowtimeResponse(showtime);

        } catch (Exception e) {
            throw new ApplicationContextException("Had the movie") ;
        }
    }
}
