package com.alibou.security.model.response;

import com.alibou.security.entity.Role;
import com.alibou.security.entity.Ticket;
import com.alibou.security.entity.Token;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {

    String username;
    String email;
    boolean status;
    String fullName;
    Date dateOfBirth;
    String phone;
    LocalDateTime createdAt = LocalDateTime.now();
    LocalDateTime updatedAt = LocalDateTime.now();
    Long createdBy;
    Long updatedBy;
    Role role;
    Set<Ticket> tickets;

}
