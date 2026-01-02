package com.example.blog.service;

import com.example.blog.domain.RefreshToken;
import com.example.blog.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository repository;

    public RefreshToken findByRefreshToken(String refreshToken) {
        return repository.findByRefreshToken(refreshToken)
                         .orElseThrow(() -> new NoSuchElementException("Unexpected Token"));
    }
}
