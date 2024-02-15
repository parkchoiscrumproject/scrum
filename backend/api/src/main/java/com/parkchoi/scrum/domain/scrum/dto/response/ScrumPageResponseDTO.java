package com.parkchoi.scrum.domain.scrum.dto.response;

import com.parkchoi.scrum.domain.scrum.entity.Scrum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class ScrumPageResponseDTO {
    private List<ScrumRoomDTO> scrums;
    private int currentPage;
    private int totalPages;
    private long totalElementes;

    public ScrumPageResponseDTO(Page<Scrum> page) {
        this.scrums = page.getContent().stream()
                .map(scrum -> ScrumRoomDTO.builder()
                        .scrumId(scrum.getId())
                        .name(scrum.getName())
                        .currentMember(scrum.getCurrentMember())
                        .profileImage(scrum.getUser().getProfileImage())
                        .isRunning(scrum.getIsStart())
                        .nickname(scrum.getUser().getNickname()).build())
                .collect(Collectors.toList());
        this.currentPage = page.getNumber();
        this.totalPages = page.getTotalPages();
        this.totalElementes = page.getTotalElements();
    }
}
