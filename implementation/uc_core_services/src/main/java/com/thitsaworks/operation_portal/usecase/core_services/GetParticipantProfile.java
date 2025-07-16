package com.thitsaworks.operation_portal.usecase.core_services;

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
                  String logoType,
                  byte[] logo,
                  Instant createdDate) { }

}
