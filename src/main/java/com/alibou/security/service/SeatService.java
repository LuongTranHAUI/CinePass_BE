package com.alibou.security.service;

import com.alibou.security.entity.Seat;
import com.alibou.security.model.request.SeatRequest;
import com.alibou.security.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static com.alibou.security.enums.SeatStatus.AVAILABLE;

@Service
@RequiredArgsConstructor
public class SeatService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SeatService.class);
    private final SeatRepository repository;
    private final UserService userService;
    private final HallService hallService;

//    public void add(SeatRequest request) {
//        var maxSeat = hallService.findById(request.getHallId()).getSeatCapacity();
//        if (request.getSeatNumber() > maxSeat) {
//            throw new IllegalArgumentException("Seat number exceeds hall capacity");
//        }
//        var seat = Seat.builder()
//                .seatRow(request.getSeatRow())
//                .seatNumber(request.getSeatNumber())
//                .status(request.getStatus())
//                .createdAt(new Timestamp(System.currentTimeMillis()).toLocalDateTime())
//                .createdBy(userService.getCurrentUserId())
//                .hall(hallService.findById(request.getHallId()))
//                .build();
//        repository.save(seat);
//        LOGGER.info("Seat added successfully: {}", seat);
//    }

    public void change(SeatRequest request) {
        var existingSeat = repository.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("Seat not found"));
        var seat = Seat.builder()
                .id(existingSeat.getId())
                .seatRow(request.getSeatRow())
                .seatNumber(request.getSeatNumber())
                .status(request.getStatus())
                .updatedAt(new Timestamp(System.currentTimeMillis()).toLocalDateTime())
                .updatedBy(userService.getCurrentUserId())
                .hall(hallService.findById(request.getHallId()))
                .build();
        repository.save(seat);
        LOGGER.info("Seat updated successfully: {}", seat);
    }

    public void delete(Long id) {
        var existingSeat = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Seat not found"));
        repository.deleteById(existingSeat.getId());
        LOGGER.info("Seat deleted successfully: {}", id);
    }

    public void deleteAll() {
        repository.deleteAll();
        LOGGER.info("All seats deleted successfully");
    }

    public void generate(Long hallId, int seatPerRow) {
        List<Seat> seats = new ArrayList<>();
        int rows = hallService.findById(hallId).getSeatCapacity() / seatPerRow;
        for (int row = 65; row < 65 + rows; row++) { // 65 is the ASCII code for 'A'
            for (int number = 1; number <= seatPerRow; number++) {
                Seat seat = Seat.builder()
                        .hall(hallService.findById(hallId))
                        .seatRow((char) row)
                        .seatNumber(number)
                        .status(AVAILABLE)
                        .createdAt(new Timestamp(System.currentTimeMillis()).toLocalDateTime())
                        .createdBy(userService.getCurrentUserId())
                        .build();
                seats.add(seat);
            }
        }
        repository.saveAll(seats);
        LOGGER.info("Seats generated successfully for hallId: {}", hallId);
    }

    public List<Seat> findAll() {
        List<Seat> seats = repository.findAll();
        LOGGER.info("Seats retrieved successfully");
        return seats;
    }

    public Seat findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Seat not found"));
    }
}
