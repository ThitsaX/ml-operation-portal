package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.type.ParticipantName;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.io.Serializable;
import java.util.List;

public interface GetOtherParticipantList
    extends UseCase<GetOtherParticipantList.Input, GetOtherParticipantList.Output> {

     record Input(ParticipantId participantId) {}

     record Output(List<ParticipantInfo> participantInfoList) {}

     record ParticipantInfo(ParticipantId participantId, ParticipantName participantName, String description)
             implements Serializable {}

}
