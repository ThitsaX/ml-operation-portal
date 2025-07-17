package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;
import com.thitsaworks.operation_portal.component.type.Mobile;

import java.time.Instant;

public interface GetParticipantProfile extends UseCase<GetParticipantProfile.Input, GetParticipantProfile.Output> {

    record Input(ParticipantId participantId) { }

    record Output(ParticipantId participantId,
                  String dfspCode,
                  String name,
                  String address,
                  Mobile mobile,
                  String logoDataType,
                  byte[] logo,
                  Instant createdDate) { }

}
