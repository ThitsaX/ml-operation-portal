package com.thitsaworks.operation_portal.usecase.participant;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantUserId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;
import com.thitsaworks.operation_portal.component.type.Email;

public interface GetExistingParticipantUser extends
                                            UseCase<GetExistingParticipantUser.Input, GetExistingParticipantUser.Output> {

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
            String dfspCode
    ) {}

}
