package com.parkchoi.scrum.util.jwt;

import com.parkchoi.scrum.domain.user.exception.AuthFailException;
import com.parkchoi.scrum.domain.user.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
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
        return path.isEmpty();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 헤더에서 액세스 토큰 추출
        Cookie[] cookies = request.getCookies();
        String accessToken = null;
        
        log.info("jwt 필터 동작");

        // 쿠키 자체가 없으면 401 에러 발생
        if(cookies == null){
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
        Long userId = jwtUtil.getUserId(accessToken);
        log.info("userId:{}", userId);

//        // 토큰 만료됐는지 확인
//        if (jwtUtil.isExpired(token)) {
//            log.error("액세스 토큰이 만료되었습니다.");
//
//            Long userId = jwtUtil.getUserId(token);
//
//            Cookie[] cookies = request.getCookies();
//            if (cookies != null) {
//                for (Cookie cookie : cookies) {
//                    if ("refreshToken".equals(cookie.getName())) {
//                        String refreshTokenCookie = cookie.getValue();
//                        String result = jwtUtil.checkRefreshToken(refreshTokenCookie, userId);
//                        if (result.equals("리프레시 토큰 만료")) {
//                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "리프레시 토큰 만료");
//                            return;
//                        } else if (result.equals("리프레시 토큰 불일치")) {
//                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "리프레시 토큰 불일치");
//                            return;
//                        }
//                    }
//                }
//            }
//            String accessJwt = jwtUtil.createAccessJwt(userId, secretKey);
//            String refreshJwt = jwtUtil.createRefreshToken(userId, secretKey);
//
//            Cookie cookie = new Cookie("refreshToken", refreshJwt);
//
//            // expires in 7 days
//            cookie.setMaxAge(14 * 24 * 60 * 60);
//
//            // optional properties
//            cookie.setSecure(false); // 이거 https 적용해서 서버로 올리면 true로 바꿔야한다. 지금은 로컬에서 테스트라서 false로 해놓음
//            cookie.setHttpOnly(true); // http only로 설정해서 javascript로 접근 못하도록 막음
//            cookie.setPath("/");
//
//            // add cookie to response
//            response.addCookie(cookie);
//
//            String jsonResponse = "{\"accessToken\":\"" + accessJwt + "\"}";
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.setContentType("application/json");
//            response.setCharacterEncoding("UTF-8");
//            response.getWriter().write(jsonResponse);
//
//            return;
//        }


        // 권한 부여
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userId, null, List.of(new SimpleGrantedAuthority("USER")));
        // Detail을 넣어준다.
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
