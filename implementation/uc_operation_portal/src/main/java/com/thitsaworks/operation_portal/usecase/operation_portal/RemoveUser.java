package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantUserId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

public interface RemoveUser extends
                                               UseCase<RemoveUser.Input, RemoveUser.Output> {

    record Input(ParticipantId participantId,
                 ParticipantUserId participantUserId) { }

    record Output(boolean removed,
                  ParticipantUserId participantUserId) { }

}
