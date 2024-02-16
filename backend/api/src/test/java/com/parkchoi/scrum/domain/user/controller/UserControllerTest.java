package com.parkchoi.scrum.domain.user.controller;

import com.parkchoi.scrum.domain.user.dto.response.UserLoginInfoResponseDTO;
import com.parkchoi.scrum.domain.user.service.UserService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;
    @InjectMocks
    private UserController userController;
    private MockMvc mockMvc;
    private String accessToken;

    @BeforeEach
    void setUp(){
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setValidator(localValidatorFactoryBean).build();
        accessToken = "test_accessToken";
    }

    @Test
    void logout() {
    }

    @Test
    void 로그인_성공() throws Exception {
        // given
        UserLoginInfoResponseDTO userLoginInfoResponseDTO = UserLoginInfoResponseDTO.builder()
                .email("test@test.com")
                .isOnline(true)
                .profileImage("이미지주소")
                .statusMessage("상태메시지")
                .nickname("닉네임").build();
//        Mockito.when(userService.getUserInfo(accessToken)).thenReturn(userLoginInfoResponseDTO);

        // when, then
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/user/login")
                        .cookie(new Cookie("accessToken", accessToken))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("로그인 성공"))
                .andExpect(jsonPath("$.data.email").value("test@test.com"))
                .andExpect(jsonPath("$.data.isOnline").value(true))
                .andExpect(jsonPath("$.data.profileImage").value("이미지주소"))
                .andExpect(jsonPath("$.data.statusMessage").value("상태메시지"))
                .andExpect(jsonPath("$.data.nickname").value("닉네임"));
    }

    @Test
    void 닉네임_중복검사_성공_사용가능() throws Exception {
        // given
        boolean result = false;
        String nickname = "asdfdsagdsafdsahgdsaf";

        // when
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/user/{nickname}/existence", nickname)
                        .cookie(new Cookie("accessToken", accessToken))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(false));
    }

    @Test
    void updateUserNickname() {
    }

    @Test
    void updateUserProfileImage() {
    }

    @Test
    void findUserInfoToEmail() {
    }

    @Test
    void changeStatusMessage() {
    }
}