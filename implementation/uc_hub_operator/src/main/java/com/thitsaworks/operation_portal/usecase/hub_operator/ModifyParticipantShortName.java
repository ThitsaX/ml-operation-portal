package com.thitsaworks.operation_portal.usecase.hub_operator;

import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.misc.usecase.AbstractAuditableUseCase;

public abstract class ModifyParticipantShortName extends
                                                 AbstractAuditableUseCase<ModifyParticipantShortName.Input, ModifyParticipantShortName.Output> {

    public record Input(
            ParticipantId participantId,
            String companyShortName,
            AccessKey accessKey
    ) {}

    public record Output(
            boolean modified,
            ParticipantId participantId
    ) {}

}
