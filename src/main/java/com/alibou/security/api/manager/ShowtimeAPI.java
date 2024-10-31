package com.alibou.security.api.manager;

import com.alibou.security.api.admin.MovieAPI;
import com.alibou.security.entity.Movie;
import com.alibou.security.entity.Showtime;
import com.alibou.security.exception.MissingRequiredFieldsException;
import com.alibou.security.mapper.ShowtimeMapper;
import com.alibou.security.model.request.MovieRequest;
import com.alibou.security.model.request.ShowtimeRequest;
import com.alibou.security.model.response.MovieResponse;
import com.alibou.security.model.response.ShowtimeResponse;
import com.alibou.security.service.ShowtimeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/management/showtime")
@RequiredArgsConstructor
@Slf4j
public class ShowtimeAPI {
    private static final Logger logger = LoggerFactory.getLogger(ShowtimeAPI.class);

    @Autowired
    ShowtimeService showtimeService;

    @PostMapping
    public ResponseEntity<?> addShowtime(@RequestBody ShowtimeRequest request) {
        try {
            ShowtimeResponse showtimeResponse = showtimeService.addShowtime(request);
            logger.info("Showtime added successfully: {}", request);
            return ResponseEntity.status(201).body(showtimeResponse);
        } catch (MissingRequiredFieldsException e) {

            logger.error("Missing required fields: {}", e.getMessage());
            return ResponseEntity.status(400).body(e.getMessage());

        } catch (Throwable e) {
            logger.error("Error saving Showtime: {}", e.getMessage());
            return ResponseEntity.status(500).body(e.getMessage());
        }

    }

    @GetMapping
    public ResponseEntity<List<Showtime>> getShowtimeList() {

        try {

            List<Showtime> showtimes = showtimeService.getAllShowtime();
            logger.info("Showtime list retrieved successfully");
            return ResponseEntity.status(200).body(showtimes);

        } catch (Exception e) {
            logger.error("Error getting showtime: {}", e.getMessage());
            return ResponseEntity.status(500).body(null);
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getShowtime(@PathVariable long id) {
        try {

            ShowtimeResponse showtimeResponse = showtimeService.getShowtimeById(id);
            logger.info("Showtime retrieved successfully: {}", showtimeResponse);
            return ResponseEntity.status(200).body(showtimeResponse);

        } catch (MissingRequiredFieldsException e) {
            logger.error("Missing required fields: {}", e.getMessage());
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error getting showtime: {}", e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateShowtimeById(@PathVariable long id, @RequestBody ShowtimeRequest showtimeRequest) {
        try {
            if (showtimeRequest == null) {
                throw new MissingRequiredFieldsException("Request body or id is missing");
            }

            ShowtimeResponse showtimeResponse = showtimeService.updateShowtime(id, showtimeRequest);
            logger.info("Showtime updated successfully: {}", showtimeRequest);
            return ResponseEntity.status(200).body(showtimeResponse);

        }catch (MissingRequiredFieldsException e) {
            logger.error("Missing required fields: {}", e.getMessage());
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error saving showtime: {}", e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteShowtime(@PathVariable long id) {
        try {
            showtimeService.deleteShowtime(id);
            logger.info("Showtime deleted successfully: {}", id);
            return ResponseEntity.status(201).body(null);
        } catch (Exception e) {
            logger.error("Error deleting showtime: {}", e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }
}
