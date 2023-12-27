package com.parkchoi.scrum.util.oauth;

import com.parkchoi.scrum.domain.user.entity.User;
import com.parkchoi.scrum.domain.user.repository.UserRepository;
import com.parkchoi.scrum.util.jwt.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;


/*
* 여기서 처리해야 되는 내용
* 1. 액세스 토큰 생성
* 2. 리프레시 토큰 생성
* 3. 쿠키에 리프레시 담음.
* 4. 클라이언트에게 필요한 dto 전달
* */
@Slf4j
@Component
public class SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Autowired
    public SuccessHandler(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }


    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("oauth 성공 핸들러 동작");
        OAuth2User principal = (OAuth2User) authentication.getPrincipal();

        // user의 이메일 추출
        Map<String, Object> attributes = principal.getAttributes();
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        String email = String.valueOf(kakaoAccount.get("email"));

        Optional<User> byEmail = userRepository.findByEmail(email);
        if(byEmail.isEmpty()){
            // 에러처리
        }else{
            User user = byEmail.get();
            // 액세스 토큰 생성
            String accessToken = jwtUtil.createAccessToken(user.getId());
            // 리프레시 토큰 생성
            String refreshToken = jwtUtil.createRefreshToken(user.getId());

            // 쿠키 생성
            Cookie cookie = new Cookie("refreshToken", refreshToken);
            cookie.setSecure(false);
            cookie.setHttpOnly(true);
            cookie.setPath("/");

            response.addCookie(cookie);

            // 이제 dto 만들어서 응답 보내줘야함.

        }

    }
}
