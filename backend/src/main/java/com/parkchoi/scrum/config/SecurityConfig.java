package com.parkchoi.scrum.config;

import com.parkchoi.scrum.util.oauth.FailureHandler;
import com.parkchoi.scrum.util.oauth.PrincipalOAuth2UserService;
import com.parkchoi.scrum.util.oauth.SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{

    private final CorsConfigurationSource corsConfigurationSource;
    private final PrincipalOAuth2UserService principalOAuth2UserService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors((cors) -> cors.configurationSource(corsConfigurationSource))
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authorize ->{
                    authorize.anyRequest().authenticated();
                })
                .oauth2Login((oauth) ->{
                    oauth.successHandler(new SuccessHandler());
                    oauth.failureHandler(new FailureHandler());
                    // oauth 로그인 성공 후 사용자 정보를 가져오기 위함.
                    // 즉 code -> accessToken 과정을 거친 후 동작.
                    oauth.userInfoEndpoint((userInfo) ->{
                        userInfo.userService(principalOAuth2UserService);
                    });
                });

        return http.build();
    }
}
