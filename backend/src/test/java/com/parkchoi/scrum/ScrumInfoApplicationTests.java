package com.parkchoi.scrum;

import com.parkchoi.scrum.domain.user.dto.response.UserLoginInfoResponseDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ScrumInfoApplicationTests {

	@Test
	@DisplayName("테스트")
	public void lombokTest(){
		//given
		String email = "qwe";
		String nickname = "qwe";
		String statusMessage = "qwe";
		String profileImage = "qwe";
		Boolean isOnline = true;

		//when
		UserLoginInfoResponseDTO build = UserLoginInfoResponseDTO.builder()
				.email(email)
				.nickname(nickname)
				.statusMessage(statusMessage)
				.profileImage(profileImage)
				.isOnline(isOnline).build();

		//then
		Assertions.assertEquals(nickname, build.getNickname());
	}

}
