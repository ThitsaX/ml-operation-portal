package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.fspiop.model.ExtensionList;
import com.thitsaworks.operation_portal.component.fspiop.model.Money;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.io.Serializable;

public interface UpdateParticipantAmount
        extends UseCase<UpdateParticipantAmount.Input, UpdateParticipantAmount.Output> {

    public record Input(
            String participantId,
            String accountId,
            String transferId,
            String externalReference,
            String action,
            String reason,
            Money amount,
            ExtensionList extensionList
    ) implements Serializable {}

    public record Output(
            String accessKey,
            String secretKey
    ) implements Serializable {}

}
