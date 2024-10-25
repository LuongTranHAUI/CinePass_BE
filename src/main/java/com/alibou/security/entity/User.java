package com.alibou.security.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "status", columnDefinition = "BIT DEFAULT 1")
    private boolean status;

    @PrePersist
    public void prePersist() {
        this.status = true;
    }

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "date_of_birth", nullable = false)
    private Date dateOfBirth;

    @Column(name = "phone", unique = true)
    private String phone;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_by")
    private Long updatedBy;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    @JsonBackReference
    private Role role;

    @Builder.Default
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Token> tokens = List.of(); // Sử dụng ManyToOne nếu cần

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Ticket> tickets;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private Set<DiscountApplication> discountApplications;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (role == null) {
            Role defaultRole = new Role();
            defaultRole.setName("USER");
            return defaultRole.getAuthorities();
        }
        return role.getAuthorities();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}

