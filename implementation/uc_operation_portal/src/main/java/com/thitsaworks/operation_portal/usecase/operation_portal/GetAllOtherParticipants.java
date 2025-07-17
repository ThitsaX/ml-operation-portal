package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.type.ParticipantInfo;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.util.List;

public interface GetAllOtherParticipants
    extends UseCase<GetAllOtherParticipants.Input, GetAllOtherParticipants.Output> {

     record Input(ParticipantId participantId) {}

     record Output(List<ParticipantInfo> participantInfoList) {}

}
