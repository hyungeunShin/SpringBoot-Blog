package com.example.springbootdeveloper.service;

import com.example.springbootdeveloper.domain.RefreshToken;
import com.example.springbootdeveloper.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository repository;

    public RefreshToken findByRefreshToken(String refreshToken) {
        return repository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected Token"));
    }
}
