package com.thitsaworks.operation_portal.core.participant.command;

import com.thitsaworks.operation_portal.component.common.identifier.ContactId;
import com.thitsaworks.operation_portal.component.common.identifier.LiquidityProfileId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.type.ContactType;
import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.component.type.Mobile;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;

import java.io.Serializable;
import java.util.List;

public interface ModifyParticipantCommand {

    record Input(ParticipantId participantId,
                 String companyName,
                 String address,
                 Mobile mobile,
                 String logoType,
                 byte[] logo) {

    }

    record Output(boolean modified,
                  ParticipantId participantId) {}

    Output execute(Input input) throws ParticipantException;

}
