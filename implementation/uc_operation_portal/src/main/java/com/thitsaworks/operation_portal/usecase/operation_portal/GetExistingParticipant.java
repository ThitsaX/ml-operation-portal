package com.thitsaworks.operation_portal.usecase.operation_portal;

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
                  String dfspCode,
                  String name,
                  String address,
                  Mobile mobile,
                  byte[] logo,
                  Instant createdDate,
                  List<ContactInfo> contactInfoList,
                  List<LiquidityProfileInfo> liquidityProfileInfoList) {

        public record ContactInfo(ContactId contactId,
                                  String name,
                                  String position,
                                  Email email,
                                  Mobile mobile,
                                  String contactType) implements Serializable { }

        public record LiquidityProfileInfo(LiquidityProfileId liquidityProfileId,
                                           String bankName,
                                           String accountName,
                                           String accountNumber,
                                           String currency,
                                           Boolean isActive) implements Serializable { }

    }

}
