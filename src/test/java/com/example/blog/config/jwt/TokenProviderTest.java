package com.example.blog.config.jwt;

import com.example.blog.domain.User;
import com.example.blog.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Duration;
import java.util.Date;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TokenProviderTest {
    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProperties jwtProperties;

    @Test
    @DisplayName("유저 정보와 만료 기간을 전달해 토큰 발급")
    void generateToken() {
        //given
        User testUser = userRepository.save(User.builder()
                                      .email("user@gmail.com")
                                      .password("test")
                                      .build());

        //when
        String token = tokenProvider.generateToken(testUser, Duration.ofDays(14));

        //then
        Long userId = tokenProvider.getUserId(token);

        assertThat(userId).isEqualTo(testUser.getId());
    }

    @Test
    @DisplayName("만료된 토큰인 경우 유효성 검증 실패")
    void validToken_invalidToken() {
        //given
        String token = JwtFactory.builder()
                                 .expiration(new Date(new Date().getTime() - Duration.ofDays(7).toMillis()))
                                 .build()
                                 .createToken(jwtProperties);

        //when
        boolean result = tokenProvider.validToken(token);

        //then
        assertThat(result).isFalse();
    }


    @Test
    @DisplayName("유효한 토큰인 경우 유효성 검증 성공")
    void validToken_validToken() {
        //given
        String token = JwtFactory.withDefaultValues()
                                 .createToken(jwtProperties);

        //when
        boolean result = tokenProvider.validToken(token);

        //then
        assertThat(result).isTrue();
    }


    @Test
    @DisplayName("토큰 기반으로 인증정보 가져오기")
    void getAuthentication() {
        //given
        String userEmail = "user@email.com";
        String token = JwtFactory.builder()
                                 .subject(userEmail)
                                 .build()
                                 .createToken(jwtProperties);

        //when
        Authentication authentication = tokenProvider.getAuthentication(token);

        //then
        assertThat(((UserDetails) authentication.getPrincipal()).getUsername()).isEqualTo(userEmail);
    }

    @Test
    @DisplayName("토큰으로 유저 ID 가져오기")
    void getUserId() {
        //given
        Long userId = 1L;
        String token = JwtFactory.builder()
                                 .claims(Map.of("id", userId))
                                 .build()
                                 .createToken(jwtProperties);

        //when
        Long userIdByToken = tokenProvider.getUserId(token);

        //then
        assertThat(userIdByToken).isEqualTo(userId);
    }
}
