package com.thitsaworks.operation_portal.usecase.hub_services;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantUserId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;
import com.thitsaworks.operation_portal.core.hub_services.data.TransferData;

import java.util.List;

public interface GetAllTransfer extends
                                UseCase<GetAllTransfer.Input, GetAllTransfer.Output> {

    record Input(String fromDate,
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
                 String timeZone) { }

    record Output(List<TransferData> transferInfoList) { }

}
