package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.type.Mobile;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

public interface GetParticipantListIncludingHub
    extends UseCase<GetParticipantListIncludingHub.Input, GetParticipantListIncludingHub.Output> {

    record Input() { }

    record Output(List<ParticipantInfo> participantInfoList) {

        public record ParticipantInfo(
            ParticipantId participantId,
            String participantName,
            String description,
            String address,
            Mobile mobile,
            String logoFileType,
            byte[] logo,
            Instant createdDate
        ) implements Serializable { }

    }

}
