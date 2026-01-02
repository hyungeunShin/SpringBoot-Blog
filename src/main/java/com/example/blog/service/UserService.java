package com.example.blog.service;

import com.example.blog.domain.User;
import com.example.blog.dto.AddUserRequest;
import com.example.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void save(AddUserRequest request) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        userRepository.save(User.builder()
                                .email(request.getEmail())
                                .password(encoder.encode(request.getPassword()))
                                .build());
    }

    public User findById(Long userId) {
        return userRepository.findById(userId)
                             .orElseThrow(() -> new NoSuchElementException("Unexpected User"));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                             .orElseThrow(() -> new NoSuchElementException("Unexpected user"));
    }
}
