package com.thitsaworks.operation_portal.usecase.central_ledger;

import com.thitsaworks.operation_portal.component.misc.usecase.AbstractAuditableUseCase;
import com.thitsaworks.operation_portal.reporting.central_ledger.data.TransferData;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantUserId;
import java.util.List;

public abstract class GetAllTransfer extends
        AbstractAuditableUseCase<GetAllTransfer.Input, GetAllTransfer.Output> {

    public record Input(
            String fromDate,
            String toDate,
            String transferId,
            String payerFspId,
            String payeeFspId,
            String payerIdentifierTypeId,
            String payeeIdentifierTypeId,
            String payerIdentifierValue,
            String payeeIdentifierValue,
            String currencyId,
            String transferStateId,
            ParticipantUserId participantUserId,
            String timeZone
    ) {}

    public record Output(List<TransferData> transferInfoList) {}
}
