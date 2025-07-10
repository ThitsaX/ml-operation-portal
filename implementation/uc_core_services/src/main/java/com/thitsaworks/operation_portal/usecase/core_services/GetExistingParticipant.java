package com.thitsaworks.operation_portal.usecase.core_services;

import com.thitsaworks.operation_portal.component.common.identifier.ContactId;
import com.thitsaworks.operation_portal.component.common.identifier.LiquidityProfileId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;
import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.component.type.Mobile;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

public interface GetExistingParticipant extends
                                        UseCase<GetExistingParticipant.Input, GetExistingParticipant.Output> {

    record Input(ParticipantId participantId) { }

    record Output(ParticipantId participantId,
                  String dfsp_code,
                  String name,
                  String address,
                  Mobile mobile,
                  Instant createdDate,
                  List<ContactInfo> contactInfoList,
                  List<LiquidityProfileInfo> liquidityProfileInfoList) {

        public record ContactInfo(ContactId contactId,
                                  String name,
                                  String title,
                                  Email email,
                                  Mobile mobile,
                                  String contactType) implements Serializable { }

        public record LiquidityProfileInfo(LiquidityProfileId liquidityProfileId,
                                           String accountName,
                                           String accountNumber,
                                           String currency,
                                           Boolean isActive) implements Serializable { }

    }

}
