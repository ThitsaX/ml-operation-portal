package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantUserId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;
import com.thitsaworks.operation_portal.component.type.Email;

public interface GetUserProfile extends
                                UseCase<GetUserProfile.Input, GetUserProfile.Output> {

    record Input(ParticipantUserId participantUserId) {}

    record Output(
            ParticipantUserId participantUserId,
            String name,
            Email email,
            String firstName,
            String lastName,
            String jobTitle,
            ParticipantId participantId,
            Long createdDate,
            String participantName,
            String description,
            String roleType
    ) {}

}
