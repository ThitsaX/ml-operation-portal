package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.type.Mobile;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.time.Instant;
import java.util.List;

public interface GetParticipantProfile
    extends UseCase<GetParticipantProfile.Input, GetParticipantProfile.Output> {

    record Input(ParticipantId participantId) { }

    record Output(ParticipantId participantId,
                  String participantName,
                  String description,
                  String address,
                  Mobile mobile,
                  String logoFileType,
                  byte[] logoBase64,
                  String connectionType,
                  List<ParticipantConnection> connectedParticipants,
                  Instant createdDate) { }

    record ParticipantConnection(String participantName, String participantDescription) {

    }

}
