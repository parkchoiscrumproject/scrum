package com.parkchoi.scrum.util.oauth;

import com.parkchoi.scrum.domain.user.entity.User;
import com.parkchoi.scrum.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;


/*
* 해당 서비스 안에 들어오면 유저 접근 권한을 얻은 상태.
* 해당 유저 여부를 판단하고 없으면 유저 정보 생성
* */
@Slf4j
@Service
@RequiredArgsConstructor
public class PrincipalOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 소셜 타입에 맞게 유동적으로 담을 인터페이스 변수 생성
        OAuth2UserInfo oAuth2UserInfo = null;

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        /*
        * 확장성을 위함.
        * kakao, naver, google 등등 다양한 타입에 맞게 수정만 해주면 됨.
        * */
        if(registrationId.equals("kakao")){
            oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
        }

        // 유저가 db에 있는지 판단.
        Optional<User> byUser = userRepository.findById(oAuth2UserInfo.getProviderId());

        /*
        * 만약에 유저가 없으면 추가 정보 페이지로 넘겨야함.
        * 해당 유저의 정보를 보존하기 위해서 세션에 저장.
        * */
        if(byUser.isEmpty()){
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true); // true: 존재하지 않으면 새로 생성
            session.setAttribute("USER_INFO", oAuth2UserInfo);
        }

        return oAuth2User;
    }
}
