package com.alibou.security.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TheaterRequest {
    @NotNull
    private String name;
    @NotNull
    private String location;
    @NotNull
    private String phone;

}
