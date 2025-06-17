package com.thitsaworks.operation_portal.usecase.participant;

import com.thitsaworks.component.common.identifier.ParticipantId;
import com.thitsaworks.component.common.type.ParticipantInfo;
import com.thitsaworks.operation_portal.component.misc.usecase.AbstractOwnableUseCase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public abstract class GetAllOtherParticipants
        extends AbstractOwnableUseCase<GetAllOtherParticipants.Input, GetAllOtherParticipants.Output> {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Input {

        private ParticipantId participantId;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Output {

        private List<ParticipantInfo> participantInfoList;

    }

}
