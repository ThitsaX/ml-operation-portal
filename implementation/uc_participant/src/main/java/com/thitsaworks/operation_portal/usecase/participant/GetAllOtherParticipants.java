package com.thitsaworks.operation_portal.usecase.participant;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.type.ParticipantInfo;
import com.thitsaworks.operation_portal.component.misc.usecase.AbstractOwnableUseCase;

import java.util.List;

public abstract class GetAllOtherParticipants
        extends AbstractOwnableUseCase<GetAllOtherParticipants.Input, GetAllOtherParticipants.Output> {

    public record Input(ParticipantId participantId) {}

    public record Output(List<ParticipantInfo> participantInfoList) {}

}
