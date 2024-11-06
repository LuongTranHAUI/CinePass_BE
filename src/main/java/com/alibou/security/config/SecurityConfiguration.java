package com.alibou.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import static com.alibou.security.enums.Permission.*;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    private static final String[] WHITE_LIST_URL = {
            "/api/auth/**"
    };

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults()) // Enable CORS
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> req
                        .requestMatchers(WHITE_LIST_URL).permitAll()
                        .requestMatchers("/api/admin/**").hasAnyRole("ADMIN")
                        .requestMatchers(GET, "/api/admin/**").hasAnyAuthority(ADMIN_READ.getPermission())
                        .requestMatchers(POST, "/api/admin/**").hasAnyAuthority(ADMIN_CREATE.getPermission())
                        .requestMatchers(PUT, "/api/admin/**").hasAnyAuthority(ADMIN_UPDATE.getPermission())
                        .requestMatchers(DELETE, "/api/admin/**").hasAnyAuthority(ADMIN_DELETE.getPermission())
                        .requestMatchers("/api/management/**").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers(GET, "/api/management/**").hasAnyAuthority(ADMIN_READ.getPermission(), MANAGER_READ.getPermission())
                        .requestMatchers(POST, "/api/management/**").hasAnyAuthority(ADMIN_CREATE.getPermission(), MANAGER_CREATE.getPermission())
                        .requestMatchers(PUT, "/api/management/**").hasAnyAuthority(ADMIN_UPDATE.getPermission(), MANAGER_UPDATE.getPermission())
                        .requestMatchers(DELETE, "/api/management/**").hasAnyAuthority(ADMIN_DELETE.getPermission(), MANAGER_DELETE.getPermission())
                        .requestMatchers("/api/user/**").hasAnyRole("ADMIN", "MANAGER", "USER")
                        .requestMatchers(GET, "/api/user/**").hasAnyAuthority(ADMIN_READ.getPermission(), MANAGER_READ.getPermission(), USER_READ.getPermission())
                        .requestMatchers(POST, "/api/user/**").hasAnyAuthority(ADMIN_CREATE.getPermission(), MANAGER_CREATE.getPermission(), USER_CREATE.getPermission())
                        .requestMatchers(PUT, "/api/user/**").hasAnyAuthority(ADMIN_UPDATE.getPermission(), MANAGER_UPDATE.getPermission(), USER_UPDATE.getPermission())
                        .requestMatchers(DELETE, "/api/user/**").hasAnyAuthority(ADMIN_DELETE.getPermission(), MANAGER_DELETE.getPermission(), USER_DELETE.getPermission())
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)) // Return 401 Unauthorized for unauthenticated access
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpStatus.FORBIDDEN.value()); // Return 403 Forbidden for authenticated but unauthorized access
                            response.getWriter().write("Forbidden");
                        })
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout")
                        .addLogoutHandler(logoutHandler)
                        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                );

        return http.build();
    }
}