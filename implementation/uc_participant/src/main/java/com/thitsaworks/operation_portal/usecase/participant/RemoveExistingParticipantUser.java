package com.thitsaworks.operation_portal.usecase.participant;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantUserId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

public interface RemoveExistingParticipantUser extends
                                               UseCase<RemoveExistingParticipantUser.Input, RemoveExistingParticipantUser.Output> {

    record Input(ParticipantId participantId,
                 ParticipantUserId participantUserId) { }

    record Output(boolean removed,
                  ParticipantUserId participantUserId) { }

}
