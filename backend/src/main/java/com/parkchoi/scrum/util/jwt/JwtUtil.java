package com.parkchoi.scrum.util.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class JwtUtil {

    @Value("${jwt.secretkey}")
    private String SECRET_KEY;
    @Value("#{${jwt.access-validity}}")
    private Long accessTokenTime;
    @Value("#{${jwt.refresh-validity}}")
    private Long refreshTokenTime;
    // redis에 저장할 키 밸류의 타입
    private final RedisTemplate<String, String> redisTemplate;

    // 유저 pk 꺼내기
    public Long getUserId(String token){
        try {
            return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token)
                    .getBody().get("userId", Long.class);
        } catch (ExpiredJwtException e) {
            // 만료된 JWT에서도 claims를 가져올 수 있습니다.
            return e.getClaims().get("userId", Long.class);
        }
    }

    // 토큰 만료 체크
    public boolean isExpired(String token){
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return false;  // 토큰 파싱에 성공하면, 만료되지 않았으므로 false를 반환.
        } catch (ExpiredJwtException e) {
            // 만료된 토큰으로 인해 예외가 발생하면, 만료된 것으로 간주하고 true를 반환.
            return true;
        }
    }

    // 액세스 토큰 생성
    public String createAccessToken(Long userId){
        Claims claims = Jwts.claims();
        claims.put("userId", userId);

        return Jwts.builder() // 액세스 토큰을 생성
                .setClaims(claims) // 유저의 pk값
                .setIssuedAt(new Date(System.currentTimeMillis())) // 현재 시간
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenTime)) // 언제까지
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) // 뭐로 사인됐는지
                .compact();
    }

    // 리프레쉬 토큰 생성
    public String createRefreshToken(Long userId){
        Claims claims = Jwts.claims();

        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenTime))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

        // redis에 저장
        redisTemplate.opsForValue().set(
                "refreshToken:" + userId, // 사용자의 이름을 key로 사용
                refreshToken,             // 리프레쉬 토큰을 value로 사용
                refreshTokenTime,              // 리프레쉬 토큰의 만료 시간
                TimeUnit.MILLISECONDS
        );
        return refreshToken;
    }

}
