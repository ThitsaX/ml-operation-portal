package com.thitsaworks.operation_portal.usecase.hub_operator;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.type.ContactType;
import com.thitsaworks.operation_portal.component.common.type.DfspCode;
import com.thitsaworks.operation_portal.component.misc.usecase.AbstractAuditableUseCase;
import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.component.type.Mobile;

import java.io.Serializable;
import java.util.List;

public abstract class CreateNewParticipant
        extends AbstractAuditableUseCase<CreateNewParticipant.Input, CreateNewParticipant.Output> {

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

    public static record Output(
        boolean created,
        ParticipantId participantId
    ) implements Serializable {}

}
