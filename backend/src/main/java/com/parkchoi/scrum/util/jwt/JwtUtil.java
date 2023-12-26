package com.parkchoi.scrum.util.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtUtil {

    @Value("${jwt.secretkey}")
    private String SECRET_KEY;
    @Value("#{${jwt.access-validity}}")
    private Long accessTokenTime;
    @Value("#{${jwt.refresh-validity}}")
    private Long refreshTokenTime;

}
