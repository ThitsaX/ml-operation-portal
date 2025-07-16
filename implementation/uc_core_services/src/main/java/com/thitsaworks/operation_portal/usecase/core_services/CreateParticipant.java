package com.thitsaworks.operation_portal.usecase.core_services;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.type.ContactType;
import com.thitsaworks.operation_portal.component.common.type.DfspCode;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;
import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.component.type.Mobile;

import java.io.Serializable;
import java.util.List;

public interface CreateParticipant
    extends UseCase<CreateParticipant.Input, CreateParticipant.Output> {

    record Input(String name,
                 DfspCode dfspCode,
                 String dfspName,
                 String address,
                 Mobile mobile,
                 String logoType,
                 byte[] logo,
                 List<ContactInfo> contactInfoList,
                 List<LiquidityProfileInfo> liquidityProfileInfoList
    ) implements Serializable {

        public record ContactInfo(String name,
                                  String title,
                                  Email email,
                                  Mobile mobile,
                                  ContactType contactType
        ) implements Serializable { }

        public record LiquidityProfileInfo(String bankName,
                                           String accountName,
                                           String accountNumber,
                                           String currency,
                                           Boolean isActive
        ) implements Serializable { }

    }

    record Output(boolean created,
                  ParticipantId participantId
    ) implements Serializable { }

}
