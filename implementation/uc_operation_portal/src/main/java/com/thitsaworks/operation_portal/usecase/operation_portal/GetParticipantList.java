package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;
import com.thitsaworks.operation_portal.component.common.type.Mobile;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

public interface GetParticipantList
    extends UseCase<GetParticipantList.Input, GetParticipantList.Output> {

    record Input() { }

    record Output(List<ParticipantInfo> participantInfoList) {

        public record ParticipantInfo(
            ParticipantId participantId,
            int dfspId,
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
