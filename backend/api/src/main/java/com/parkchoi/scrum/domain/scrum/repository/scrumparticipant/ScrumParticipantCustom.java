package com.parkchoi.scrum.domain.scrum.repository.scrumparticipant;

import com.parkchoi.scrum.domain.scrum.entity.Scrum;
import com.parkchoi.scrum.domain.user.entity.User;

public interface ScrumParticipantCustom {
    Boolean existsEnterScrum(User user, Scrum scrum);
}
