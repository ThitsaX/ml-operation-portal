package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.io.Serializable;

public interface UpdateParticipantStatus
    extends UseCase<UpdateParticipantStatus.Input, UpdateParticipantStatus.Output> {

    public record Input(
        String participantName,
        int participantCurrencyId,
        String activeStatus

    ) implements Serializable { }

    public record Output(
        String participantName,
        int participantCurrencyId,
        String activeStatus
    ) implements Serializable { }

}
