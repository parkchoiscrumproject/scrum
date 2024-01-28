package com.parkchoi.scrum.domain.user.dto.response;

import lombok.Data;

@Data
public class UserProfileImageUpdateResponseDTO {
    private String profileImage;

    public UserProfileImageUpdateResponseDTO(String profileImage) {
        this.profileImage = profileImage;
    }
}
