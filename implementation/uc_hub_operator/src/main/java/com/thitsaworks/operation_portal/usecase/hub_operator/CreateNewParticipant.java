package com.thitsaworks.operation_portal.usecase.hub_operator;

import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.component.type.Mobile;
import com.thitsaworks.operation_portal.component.usecase.AbstractAuditableUseCase;
import com.thitsaworks.operation_portal.participant.identity.ParticipantId;
import com.thitsaworks.operation_portal.participant.type.DfspCode;
import java.io.Serializable;
import java.util.List;

public abstract class CreateNewParticipant
        extends AbstractAuditableUseCase<CreateNewParticipant.Input, CreateNewParticipant.Output> {

    public static record Input(
        String name,
        DfspCode dfspCode,
        String dfspName,
        String address,
        Mobile mobile,
        List<ContactInfo> contactInfoList,
        List<LiquidityProfileInfo> liquidityProfileInfoList
    ) implements Serializable {
        public static record ContactInfo(
            String name,
            String title,
            Email email,
            Mobile mobile,
            String contactType
        ) implements Serializable {}

        public static record LiquidityProfileInfo(
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
