package com.thitsaworks.operation_portal.core.participant.command;

import com.thitsaworks.operation_portal.component.common.identifier.ContactId;
import com.thitsaworks.operation_portal.component.common.identifier.LiquidityProfileId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.type.ContactType;
import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.component.type.Mobile;
import com.thitsaworks.operation_portal.core.participant.exception.ContactNotFoundException;
import com.thitsaworks.operation_portal.core.participant.exception.LiquidityProfileNotFoundException;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantNotFoundException;

import java.io.Serializable;
import java.util.List;

public interface ModifyParticipant {

    record Input(ParticipantId participantId,
                 String companyName,
                 String address,
                 Mobile mobile,
                 List<ContactInfo> contactInfoList,
                 List<Input.LiquidityProfileInfo> liquidityProfileInfoList) {

        public record ContactInfo(ContactId contactId,
                                  String name,
                                  String title,
                                  Email email,
                                  Mobile mobile,
                                  ContactType contactType) implements Serializable {

        }

        public record LiquidityProfileInfo(LiquidityProfileId liquidityProfileId,
                                           String accountName,
                                           String accountNumber,
                                           String currency,
                                           Boolean isActive) implements Serializable {

        }

    }

    record Output(boolean modified,
                  ParticipantId participantId) {}

    Output execute(Input input)
            throws ParticipantNotFoundException, ContactNotFoundException, LiquidityProfileNotFoundException;

}
