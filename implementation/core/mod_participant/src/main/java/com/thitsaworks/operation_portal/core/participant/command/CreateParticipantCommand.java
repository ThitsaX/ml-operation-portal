package com.thitsaworks.operation_portal.core.participant.command;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.type.ContactType;
import com.thitsaworks.operation_portal.component.common.type.ParticipantName;
import com.thitsaworks.operation_portal.component.common.type.Email;
import com.thitsaworks.operation_portal.component.common.type.Mobile;
import com.thitsaworks.operation_portal.component.common.type.ParticipantStatus;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;

import java.io.Serializable;
import java.util.List;

public interface CreateParticipantCommand {

    record Input(ParticipantName participantName,
                 String description,
                 String address,
                 Mobile mobile,
                 ParticipantStatus participantStatus,
                 List<ContactInfo> contactInfoList,
                 List<LiquidityProfileInfo> liquidityProfileInfoList
    ) implements Serializable {

        public record ContactInfo(String name,
                                  String position,
                                  Email email,
                                  Mobile mobile,
                                  ContactType contactType
        ) implements Serializable { }

        public record LiquidityProfileInfo(String accountName,
                                           String accountNumber,
                                           String currency,
                                           Boolean isActive
        ) implements Serializable { }

    }

    record Output(boolean created, ParticipantId participantId) { }

    Output execute(Input input) throws ParticipantException;

}
