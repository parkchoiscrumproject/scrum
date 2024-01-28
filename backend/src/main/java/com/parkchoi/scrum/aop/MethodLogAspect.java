package com.parkchoi.scrum.aop;

import com.parkchoi.scrum.domain.user.dto.response.UserLoginInfoResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class MethodLogAspect {

    @Before("execution(* com.parkchoi.scrum.domain.user.controller.UserController.login(..))")
    public void beforeLoginExecution(){
        log.info("로그인 컨트롤러 시작");
    }

    @AfterReturning(pointcut = "execution(* com.parkchoi.scrum.domain.user.controller.UserController.login(..))")
    public void afterLoginExecution(){
        log.info("로그인 컨트롤러 성공");
    }
}
