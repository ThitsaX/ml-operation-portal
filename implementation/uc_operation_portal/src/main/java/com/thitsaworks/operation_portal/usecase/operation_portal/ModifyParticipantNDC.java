package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantNDCId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.math.BigDecimal;

public interface ModifyParticipantNDC extends UseCase<ModifyParticipantNDC.Input, ModifyParticipantNDC.Output> {

    record Input(ParticipantNDCId participantNDCId,
                 String dfspCode,
                 String currency,
                 BigDecimal ndcPercent,
                 BigDecimal ndcAmount) {}

    record Output(ParticipantNDCId participantNDCId) {}

}
