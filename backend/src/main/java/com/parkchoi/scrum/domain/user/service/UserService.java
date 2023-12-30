package com.parkchoi.scrum.domain.user.service;

import com.parkchoi.scrum.domain.log.entity.UserLog;
import com.parkchoi.scrum.domain.log.repository.UserLogRepository;
import com.parkchoi.scrum.domain.user.dto.request.AccessTokenRequestDTO;
import com.parkchoi.scrum.domain.user.entity.User;
import com.parkchoi.scrum.domain.user.exception.UserNotFoundException;
import com.parkchoi.scrum.domain.user.repository.UserRepository;
import com.parkchoi.scrum.util.GetUserId;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserLogRepository userLogRepository;
    private final GetUserId getUserId;
    private final PasswordEncoder passwordEncoder;

    // 서비스 로그인
    @Transactional
    public void login(AccessTokenRequestDTO accessTokenRequestDTO){

        User user = getUserId.getUserId();
        String tempAccessToken = user.getTempAccessToken();
        if (tempAccessToken.equals(null)){
            throw new UserNotFoundException("로그인 과정에 문제가 발생하였습니다.(임시 토큰 null)");
        }else{
            // 두 개의 accessToken이 같이면 로그인 정상 진행
            if (passwordEncoder.matches(accessTokenRequestDTO.getAccessToken(), tempAccessToken)){
                // 로그인 기록 생성
                UserLog build = UserLog.builder()
                        .user(user).build();

                userLogRepository.save(build);
                user.setTempAccessToken(null);
            }else{
                throw new UserNotFoundException("로그인 과정에 문제가 발생하였습니다.(액세스 토큰 불일치)");
            }
        }


    }

}
