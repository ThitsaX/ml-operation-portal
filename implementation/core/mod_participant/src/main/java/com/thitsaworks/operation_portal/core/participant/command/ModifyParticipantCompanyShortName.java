package com.thitsaworks.operation_portal.core.participant.command;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;

public interface ModifyParticipantCompanyShortName {

    record Input(
            ParticipantId participantId,
            String companyShortName) {}

    record Output(
            boolean modified,
            ParticipantId participantId) {}

    Output execute(Input input) throws ParticipantException;

}
