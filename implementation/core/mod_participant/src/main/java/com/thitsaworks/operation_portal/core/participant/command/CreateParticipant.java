package com.thitsaworks.operation_portal.core.participant.command;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.type.ContactType;
import com.thitsaworks.operation_portal.component.common.type.DfspCode;
import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.component.type.Mobile;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantAlreadyRegisteredException;

import java.io.Serializable;
import java.util.List;

public interface CreateParticipant {

    public record Input(
            String name,
            DfspCode dfspCode,
            String dfspName,
            String address,
            Mobile mobile,
            List<ContactInfo> contactInfoList,
            List<LiquidityProfileInfo> liquidityProfileInfoList
    ) implements Serializable {

        public record ContactInfo(
                String name,
                String title,
                Email email,
                Mobile mobile,
                ContactType contactType
        ) implements Serializable {}

        public record LiquidityProfileInfo(
                String accountName,
                String accountNumber,
                String currency,
                Boolean isActive
        ) implements Serializable {}

    }

    record Output(boolean created, ParticipantId participantId) {}

    Output execute(Input input) throws ParticipantAlreadyRegisteredException;

}
