package com.alibou.security.api.admin;

import com.alibou.security.model.request.*;
import com.alibou.security.service.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/admin/fake-data")
public class FakeDataAPI {

    @GetMapping("/register")
    public List<RegisterRequest> generateFakeRegisterRequests() {
        return IntStream.range(0, 10)
                .mapToObj(i -> AuthenticationService.generateFakeRegisterRequest())
                .collect(Collectors.toList());
    }

    @GetMapping("/authentication")
    public List<AuthenticationRequest> generateFakeAuthenticationRequests() {
        return IntStream.range(0, 10)
                .mapToObj(i -> AuthenticationService.generateFakeAuthenticationRequest())
                .collect(Collectors.toList());
    }

    @GetMapping("/discount")
    public List<DiscountRequest> generateFakeDiscountRequests() {
        return IntStream.range(0, 10)
                .mapToObj(i -> DiscountService.generateFakeDiscountRequest())
                .collect(Collectors.toList());
    }

    @GetMapping("/hall")
    public List<HallRequest> generateFakeHallRequests() {
        return IntStream.range(0, 10)
                .mapToObj(i -> HallService.generateFakeHallRequest())
                .collect(Collectors.toList());
    }

    @GetMapping("/movie")
    public List<MovieRequest> generateFakeMovieRequests() {
        return IntStream.range(0, 10)
                .mapToObj(i -> MovieService.generateFakeMovieRequest())
                .collect(Collectors.toList());
    }

    @GetMapping("/notification")
    public List<NotificationRequest> generateFakeNotificationRequests() {
        return IntStream.range(0, 10)
                .mapToObj(i -> NotificationService.generateFakeNotificationRequest())
                .collect(Collectors.toList());
    }

    @GetMapping("/showtime")
    public List<ShowtimeRequest> generateFakeShowtimeRequests() {
        return IntStream.range(0, 10)
                .mapToObj(i -> ShowtimeService.generateFakeShowtimeRequest())
                .collect(Collectors.toList());
    }
}