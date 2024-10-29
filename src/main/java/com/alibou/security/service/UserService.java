package com.alibou.security.service;

import com.alibou.security.entity.User;
import com.alibou.security.mapper.UserMapper;
import com.alibou.security.model.response.UserResponse;
import com.alibou.security.repository.UserRepository;
import com.alibou.security.model.request.ChangePasswordRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {
        if (request == null) {
            throw new IllegalArgumentException("ChangePasswordRequest cannot be null");
        }

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // check if the current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            logger.warn("Wrong current password for user: {}", user.getUsername());
            throw new IllegalStateException("Wrong password");
        }

        // check if the two new passwords are the same
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            logger.warn("New passwords do not match for user: {}", user.getUsername());
            throw new IllegalStateException("Passwords are not the same");
        }

        // update the password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // save the new password
        repository.save(user);
        logger.info("Password changed successfully for user: {}", user.getUsername());
    }

    public Long getCurrentUserId() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var user = (User) authentication.getPrincipal();
        return user.getId();
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(Math.toIntExact(id)).orElseThrow(() -> new ApplicationContextException("Not found user"));

        return userMapper.toUserResponse(user);
    }

    public UserResponse blockUser(long id) {

        User user = userRepository.findById(Math.toIntExact(id)).orElseThrow(() -> new ApplicationContextException("Not found user"));
        if(user.isStatus()){
            user.setStatus(false);
            repository.save(user);
        }
        logger.info("User blocked: {}", user.getUsername());
        return userMapper.toUserResponse(user);
    }

}
