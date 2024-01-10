package com.parkchoi.scrum.domain.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockCookie;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

// 특정 클래스의 빈만 로드하기 위해서 사용
@WebMvcTest(UserControllerTest.class)
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    private ObjectMapper objectMapper;


    // 테스트 전에 MockMvc 인스턴스 초기화
    @BeforeEach
    public void setup(){
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(new CharacterEncodingFilter("UTF-8, true"))
                .alwaysDo(print())
                .build();
    }

    @Test
    @DisplayName("유저 로그인 api 테스트")
    public void 유저_로그인() throws Exception{

        MockCookie cookie = new MockCookie("accessToken", "테스트용 토큰");

        mockMvc.perform(post("/api/user/login")
                .cookie(cookie));

    }
}
