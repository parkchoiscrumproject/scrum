package com.parkchoi.scrum.aop;

import com.parkchoi.scrum.domain.user.dto.response.UserInfoResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class MethodLogAspect {

    private long startTime;

    @Before("execution(* com.parkchoi.scrum.domain.user.service.UserService.getUserInfo(..))")
    public void beforeMethodExecution(){
        startTime = System.currentTimeMillis();
        log.info("로그인 로그 시작");
    }

    @AfterReturning(pointcut = "execution(* com.parkchoi.scrum.domain.user.service.UserService.getUserInfo(..))", returning = "response")
    public void afterMethodExecution(UserInfoResponseDTO response){
        long endTime = System.currentTimeMillis();
        log.info("로그인 로그 등록 완료");
        log.info("실행 시간(ms) : {} 밀리초", (endTime - startTime));
    }
}
