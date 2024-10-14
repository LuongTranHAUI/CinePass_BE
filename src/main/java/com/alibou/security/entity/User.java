package com.alibou.security.entity;

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
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {

  @Id
  @GeneratedValue
  private Integer id;

  @NotBlank
  @Column(name = "username", nullable = false, unique = true)
  private String username;

  @Column(name = "status", columnDefinition = "BIT DEFAULT 1")
  private boolean status;

  @PrePersist
  public void prePersist() {
    this.status = true;
  }

  @Column(name = "date_of_birth", nullable = false)
  private Date dateOfBirth;

  @Column(name = "phone", unique = true)
  private String phone;

  @Column(name = "full_name", nullable = false)
  private String fullName;

  @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
  private Timestamp createdAt;

  @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
  private Timestamp updatedAt;

  @ManyToOne(fetch = FetchType.EAGER) // Changed to EAGER fetching
  @JoinColumn(name = "role_id")
  private Role role;

  @Email
  @NotBlank
  @Column(unique = true)
  private String email;

  @NotBlank
  private String password;

  @Builder.Default
  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  private List<Token> tokens = List.of();

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

  @Override
  public boolean isEnabled() {
    return true;
  }
}