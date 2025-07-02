package com.thitsaworks.operation_portal.usecase.common;

import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.common.identifier.ContactId;
import com.thitsaworks.operation_portal.component.common.identifier.LiquidityProfileId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.type.ContactType;
import com.thitsaworks.operation_portal.component.misc.usecase.AbstractAuditableUseCase;
import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.component.type.Mobile;

import java.io.Serializable;
import java.util.List;

public abstract class ModifyExistingParticipant extends
                                                AbstractAuditableUseCase<ModifyExistingParticipant.Input, ModifyExistingParticipant.Output> {

    public record Input(
            ParticipantId participantId,
            String companyName,
            String address,
            Mobile mobile,
            List<Input.ContactInfo> contactInfoList,
            List<Input.LiquidityProfileInfo> liquidityProfileInfoList,
            AccessKey accessKey) {

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

    public record Output(boolean modified, ParticipantId participantId) {


    }

}
