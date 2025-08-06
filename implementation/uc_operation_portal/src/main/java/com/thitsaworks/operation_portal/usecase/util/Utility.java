package com.thitsaworks.operation_portal.usecase.util;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantUserId;
import com.thitsaworks.operation_portal.core.participant.query.ParticipantUserQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Utility {

    private final ParticipantUserQuery participantUserQuery;

    public String getEmail(ParticipantUserId participantUserId) {

        return this.participantUserQuery.find(participantUserId)
                                        .map(user -> user.email()
                                                 .getValue())
                                        .orElse("unknown");
    }


}
