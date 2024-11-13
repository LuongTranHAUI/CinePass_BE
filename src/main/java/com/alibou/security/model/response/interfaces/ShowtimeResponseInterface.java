package com.alibou.security.model.response.interfaces;

import java.time.LocalDateTime;

public interface ShowtimeResponseInterface {
    LocalDateTime getShowTime();
    String getMovieTitle();
    String getTheaterName();
    String getHallName();
}
