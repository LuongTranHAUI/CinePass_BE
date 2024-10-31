package com.alibou.security.service;

import com.alibou.security.config.GeneralMapper;
import com.alibou.security.entity.Role;
import com.alibou.security.entity.User;
import com.alibou.security.model.request.ChangePasswordRequest;
import com.alibou.security.model.response.UserResponse;
import com.alibou.security.repository.RoleRepository;
import com.alibou.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    private final RoleRepository roleRepository;
    private final GeneralMapper generalMapper;

    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {
        if (request == null) {
            logger.warn("ChangePasswordRequest is null");
            throw new IllegalArgumentException("ChangePasswordRequest cannot be null");
        }

        if (!(connectedUser instanceof UsernamePasswordAuthenticationToken)) {
            logger.warn("Connected user is not an instance of UsernamePasswordAuthenticationToken");
            throw new IllegalStateException("Invalid user authentication");
        }

        var authenticationToken = (UsernamePasswordAuthenticationToken) connectedUser;
        var user = (User) authenticationToken.getPrincipal();

        // Check if the current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            logger.warn("Wrong current password for user: {}", user.getUsername());
            throw new IllegalStateException("Wrong password");
        }

        // Check if the two new passwords are the same
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            logger.warn("New passwords do not match for user: {}", user.getUsername());
            throw new IllegalStateException("Passwords are not the same");
        }

        // Update the password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // Save the new password
        repository.save(user);
        logger.info("Password changed successfully for user: {}", user.getUsername());
    }

    public Long getCurrentUserId() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            logger.warn("No authenticated user found");
            throw new IllegalStateException("No authenticated user found");
        }
        var user = (User) authentication.getPrincipal();
        return user.getId();
    }

    public User getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            logger.warn("No authenticated user found");
            throw new IllegalStateException("No authenticated user found");
        }
        return (User) authentication.getPrincipal();
    }

    public User getUserById(Long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalStateException("User not found"));
    }

    public UserResponse getUserInfoById(Long id) {
        User users = repository.findById(id).orElseThrow(() -> new IllegalStateException("User not found"));
        return generalMapper.mapToDTO(users, UserResponse.class);
    }

    public List<UserResponse> getAllUsers() {
        List<User> users = repository.findAll();
        logger.info("Retrieved all users successfully");
        return users.stream()
                .map(user -> generalMapper.mapToDTO(user, UserResponse.class))
                .toList();
    }

    public UserResponse changeRole(Long id, Long roleId) {
        User user = repository.findById(id).orElseThrow(() -> new IllegalStateException("User not found"));
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new IllegalStateException("Role not found"));
        user.setRole(role);
        repository.save(user);
        logger.info("Role changed successfully for user with id: {}", id);
        return generalMapper.mapToDTO(user, UserResponse.class);
    }

    public void deleteUserById(Long id) {
        repository.deactivateUserById(id);
        logger.info("User with id {} has been deactivated", id);
    }
}