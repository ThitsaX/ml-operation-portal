package com.thitsaworks.operation_portal.usecase.core_services;

import com.thitsaworks.operation_portal.component.fspiop.model.Money;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.io.Serializable;

public interface UpdateParticipantAmount
        extends UseCase<UpdateParticipantAmount.Input, UpdateParticipantAmount.Output> {

    public record Input(
            String transferId,
            String externalReference,
            String action,
            String reason,
            Money amount
    ) implements Serializable {}

    public record Output(
            String accessKey,
            String secretKey
    ) implements Serializable {}

}
