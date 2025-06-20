package com.thitsaworks.operation_portal.usecase.hub_operator;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.misc.usecase.AbstractAuditableUseCase;
import com.thitsaworks.operation_portal.component.type.Mobile;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

public abstract class GetAllParticipant
        extends AbstractAuditableUseCase<GetAllParticipant.Input, GetAllParticipant.Output> {

    public record Input() {}

    public record Output(List<ParticipantInfo> participantInfoList) {
        public record ParticipantInfo(
                ParticipantId participantId,
                String dfsp_code,
                String name,
                String dfsp_name,
                String address,
                Mobile mobile,
                String businessContact,
                String technicalContact,
                Instant createdDate
        ) implements Serializable {}
    }
}
