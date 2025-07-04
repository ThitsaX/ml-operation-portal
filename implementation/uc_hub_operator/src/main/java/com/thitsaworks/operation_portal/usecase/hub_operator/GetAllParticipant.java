package com.thitsaworks.operation_portal.usecase.hub_operator;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.misc.usecase.AbstractAuditableUseCase;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;
import com.thitsaworks.operation_portal.component.type.Mobile;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

public interface GetAllParticipant
    extends UseCase<GetAllParticipant.Input, GetAllParticipant.Output> {

    record Input() {}

    record Output(List<ParticipantInfo> participantInfoList) {
        public record ParticipantInfo(
                ParticipantId participantId,
                String dfsp_code,
                String name,
                String dfsp_name,
                String address,
                Mobile mobile,
                Instant createdDate
        ) implements Serializable {}
    }
}
