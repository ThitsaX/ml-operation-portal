package com.thitsaworks.operation_portal.usecase.core_services;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantNDCId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;
import com.thitsaworks.operation_portal.component.type.Email;

import java.math.BigDecimal;

public interface CreateParticipantNDC extends UseCase<CreateParticipantNDC.Input, CreateParticipantNDC.Output> {

    record Input(String dfspCode,
                 String currency,
                 BigDecimal ndcPercent,
                 BigDecimal ndcAmount) { }

    record Output(ParticipantNDCId participantNDCId) { }
}
