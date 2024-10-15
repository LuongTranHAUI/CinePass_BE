package com.alibou.security.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TheaterResponse {

    private Integer id;
    private String name;
    private String location;
    private int totalSeats;
}
