package com.parkchoi.scrum.util.jwt;

import com.parkchoi.scrum.domain.user.service.UserService;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final UserService userService;
    @Value("${jwt.secretkey}")
    private String SECRET_KEY;
    @Value("#{${jwt.access-validity}}")
    private Long accessTokenTime;
    @Value("#{${jwt.refresh-validity}}")
    private Long refreshTokenTime;
    private final JwtUtil jwtUtil;


    @Override // 이 주소로 오는 건 토큰 없어도 됨.
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.startsWith("/swagger-resoureces/") || path.startsWith("/swagger-ui/") || path.startsWith("/v3/api-docs/")
                || path.startsWith("/api-docs") || path.startsWith("/favicon.ico");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 헤더에서 액세스 토큰 추출
        Cookie[] cookies = request.getCookies();
        String accessToken = null;

        log.info("jwt 필터 동작");

        // 쿠키 자체가 없으면 401 에러 발생
        if (cookies == null) {
            log.error("쿠키가 존재하지 않습니다.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 상태 코드 설정
            response.setContentType("application/json"); // 컨텐츠 타입을 JSON으로 설정
            response.setCharacterEncoding("UTF-8"); // 문자 인코딩 설정
            response.getWriter().write("{\n" +
                    "  \"status\": \"error\",\n" +
                    "  \"data\": null,\n" +
                    "  \"message\": \"쿠키가 존재하지 않습니다.\"\n" +
                    "}"); // JSON 형식의 에러 메시지 작성
            return; // 여기서 처리 종료
        }

        for (Cookie c : cookies) {
            if (c.getName().equals("accessToken")) {
                accessToken = c.getValue();
                break;
            }
        }

        // 토큰이 null인 경우(없는 경우)
        if (accessToken == null) {
            log.error("액세스 토큰이 없습니다.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 상태 코드 설정
            response.setContentType("application/json"); // 컨텐츠 타입을 JSON으로 설정
            response.setCharacterEncoding("UTF-8"); // 문자 인코딩 설정
            response.getWriter().write("{\n" +
                    "  \"status\": \"error\",\n" +
                    "  \"data\": null,\n" +
                    "  \"message\": \"액세스 토큰이 없습니다.\"\n" +
                    "}"); // JSON 형식의 에러 메시지 작성
            return; // 여기서 처리 종료
        }

        // userId 토큰에서 꺼냄.
        try{
            Long userId = jwtUtil.getUserId(accessToken);

            log.info("userId:{}", userId);

            // 토큰이 만료됐으면
            if (jwtUtil.isExpired(accessToken)) {
                // 리프레시 토큰 탐색
                String refreshToken = null;
                for (Cookie c : cookies) {
                    if (c.getName().equals("refreshToken")) {
                        refreshToken = c.getValue();
                        break;
                    }
                }
                // 만약에 null이면
                if (refreshToken == null) {
                    log.error("리프레시 토큰이 존재하지 않습니다.");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 상태 코드 설정
                    response.setContentType("application/json"); // 컨텐츠 타입을 JSON으로 설정
                    response.setCharacterEncoding("UTF-8"); // 문자 인코딩 설정
                    response.getWriter().write("{\n" +
                            "  \"status\": \"error\",\n" +
                            "  \"data\": null,\n" +
                            "  \"message\": \"리프레시 토큰이 존재하지 않습니다.\"\n" +
                            "}"); // JSON 형식의 에러 메시지 작성
                    return; // 여기서 처리 종료
                }

                String key = "refreshToken:" + userId;
                String result = jwtUtil.checkRefreshToken(key, refreshToken, response);

                if (result.equals("리프레시 토큰 만료") || result.equals("리프레시 토큰 불일치")) {
                    log.error("리프레시 토큰 문제 발생");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 상태 코드 설정
                    response.setContentType("application/json"); // 컨텐츠 타입을 JSON으로 설정
                    response.setCharacterEncoding("UTF-8"); // 문자 인코딩 설정
                    response.getWriter().write("{\n" +
                            "  \"status\": \"error\",\n" +
                            "  \"data\": null,\n" +
                            "  \"message\": \"리프레시 토큰 문제 발생(로그아웃 진행)\"\n" +
                            "}"); // JSON 형식의 에러 메시지 작성
                    return; // 여기서 처리 종료
                } else {
                    String reAccessToken = jwtUtil.createAccessToken(userId);
                    String reRefreshToken = jwtUtil.createRefreshToken(userId);

                    // 액세스 토큰을 위한 쿠키 생성
                    Cookie accessTokenCookie = new Cookie("accessToken", reAccessToken);
                    accessTokenCookie.setHttpOnly(true);
                    accessTokenCookie.setSecure(true); // HTTPS를 사용하는 경우에만 true로 설정
                    accessTokenCookie.setPath("/");

                    // 리프레시 토큰을 위한 쿠키 생성
                    Cookie refreshTokenCookie = new Cookie("refreshToken", reRefreshToken);
                    refreshTokenCookie.setHttpOnly(true);
                    refreshTokenCookie.setSecure(true); // HTTPS를 사용하는 경우에만 true로 설정
                    refreshTokenCookie.setPath("/");

                    // 쿠키에 토큰 저장
                    response.addCookie(refreshTokenCookie);
                    response.addCookie(accessTokenCookie);

                    log.error("액세스 토큰 재발급");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 상태 코드 설정
                    response.setContentType("application/json"); // 컨텐츠 타입을 JSON으로 설정
                    response.setCharacterEncoding("UTF-8"); // 문자 인코딩 설정
                    response.getWriter().write("{\n" +
                            "  \"status\": \"error\",\n" +
                            "  \"data\": null,\n" +
                            "  \"message\": \"액세스 토큰 재발급\"\n" +
                            "}"); // JSON 형식의 에러 메시지 작성
                    return; // 여기서 처리 종료
                }
            }


            // 권한 부여
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userId, null, List.of(new SimpleGrantedAuthority("USER")));
            // Detail을 넣어준다.
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request, response);
        }catch (SignatureException e){
            log.error("잘못된 JWT 서명입니다.", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 상태 코드 설정
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\n" +
                    "  \"status\": \"error\",\n" +
                    "  \"data\": null,\n" +
                    "  \"message\": \"잘못된 서명의 토큰입니다.\"\n" +
                    "}");
            return; // 필터 체인 처리 중지
        }

    }

    // 리다이렉트 주소
    private String makeRedirectUrl() {
        return UriComponentsBuilder.fromUriString("http://localhost:3000/refreshToken")
                .encode(StandardCharsets.UTF_8)
                .build().toUriString();
    }
}