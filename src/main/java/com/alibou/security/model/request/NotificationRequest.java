package com.alibou.security.model.request;

import com.alibou.security.enums.NotificationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {
    private Long id;

    @NotNull
    private String type;

    @NotNull
    private String message;

    @NotNull
    private NotificationStatus status;
}
