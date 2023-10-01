package com.example.springbootdeveloper.config.jwt;

import com.example.springbootdeveloper.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class TokenProvider {
    private final JwtProperties jwtProperties;

    public String generateToken(User user, Duration expiredAt) {
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), user);
    }

    private String makeToken(Date expiry, User user) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)                       //헤더 type: JWT
                .setIssuer(jwtProperties.getIssuer())                               //내용 issuer: properties에 설정한 값
                .setIssuedAt(now)                                                   //발행: 현재
                .setExpiration(expiry)                                              //만료: 파라미터
                .setSubject(user.getEmail())                                        //내용: 유저 이메일
                .claim("id", user.getId())                                    //클레임 id: 유저 ID
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretkey())   //서명: 비밀값과 함께 해시값을 HS256방식으로 암호화
                .compact();
    }

    //JWT 유효성 검증
    public boolean validToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretkey())      //복호화
                    .parseClaimsJws(token);

            return true;
        } catch(Exception e) {  //복호화 과정에서 에러가 나면 유효하지 않은 토큰
            return false;
        }
    }

    //토큰 기반으로 인증 정보를 가져오는 메소드
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        return new UsernamePasswordAuthenticationToken(new org.springframework.security.core.userdetails.User(claims.getSubject(),
                "", authorities), token, authorities);
    }

    public Long getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecretkey())
                .parseClaimsJws(token)
                .getBody();
    }
}
