package com.parkchoi.scrum.util;

import com.parkchoi.scrum.domain.user.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RandomNickname {

    private final UserRepository userRepository;

    public String generateUniqueNickname(){
        String nickname;
        do {
            List<String> nick = Arrays.asList("기분나쁜", "기분좋은", "신바람나는", "상쾌한", "짜릿한", "그리운", "자유로운", "서운한", "울적한", "비참한", "위축되는", "긴장되는", "두려운", "당당한", "배부른", "수줍은", "창피한", "멋있는",
                    "열받은", "심심한", "잘생긴", "이쁜", "시끄러운");
            List<String> name = Arrays.asList("사자", "코끼리", "호랑이", "곰", "여우", "늑대", "너구리", "침팬치", "고릴라", "참새", "고슴도치", "강아지", "고양이", "거북이", "토끼", "앵무새", "하이에나", "돼지", "하마", "원숭이", "물소", "얼룩말", "치타",
                    "악어", "기린", "수달", "염소", "다람쥐", "판다");
            List<String> num = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "0");
            Collections.shuffle(nick);
            Collections.shuffle(name);
            Collections.shuffle(num);

            nickname = nick.get(0) + name.get(0) + num.get(0) + num.get(1) + num.get(2) + num.get(3) + num.get(4);
        } while (userRepository.existsByNickname(nickname)); // 닉네임이 이미 존재하면 다시 생성
        return nickname;
    }

}
