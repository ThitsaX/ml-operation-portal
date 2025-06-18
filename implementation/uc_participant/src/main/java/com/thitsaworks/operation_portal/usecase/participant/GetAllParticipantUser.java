package com.thitsaworks.operation_portal.usecase.participant;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantUserId;
import com.thitsaworks.operation_portal.component.misc.usecase.AbstractAuditableUseCase;
import com.thitsaworks.operation_portal.component.type.Email;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

public abstract class GetAllParticipantUser extends
        AbstractAuditableUseCase<GetAllParticipantUser.Input, GetAllParticipantUser.Output> {

    public record Input(ParticipantId participantId) {}

    public record Output(List<UserInfo> userInfoList) {}

    public record UserInfo(
            ParticipantUserId participantUserId,
            String name,
            Email email,
            String firstName,
            String lastName,
            String jobTitle,
            String roleType,
            String status,
            Instant createdDate
    ) implements Serializable {}

}
