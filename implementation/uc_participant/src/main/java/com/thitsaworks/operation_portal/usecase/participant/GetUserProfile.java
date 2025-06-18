package com.thitsaworks.operation_portal.usecase.participant;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantUserId;
import com.thitsaworks.operation_portal.component.misc.usecase.AbstractOwnableUseCase;
import com.thitsaworks.operation_portal.component.type.Email;

public abstract class GetUserProfile extends
        AbstractOwnableUseCase<GetUserProfile.Input, GetUserProfile.Output> {

    public record Input(ParticipantUserId participantUserId) {}

    public record Output(
            ParticipantUserId participantUserId,
            String name,
            Email email,
            String firstName,
            String lastName,
            String jobTitle,
            ParticipantId participantId,
            Long createdDate,
            String dfspCode,
            String dfspName,
            String roleType
    ) {}

}
