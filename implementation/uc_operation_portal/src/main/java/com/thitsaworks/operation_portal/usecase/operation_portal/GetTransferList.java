package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;
import com.thitsaworks.operation_portal.core.hub_services.data.TransferData;

import java.util.List;

public interface GetTransferList extends
                                UseCase<GetTransferList.Input, GetTransferList.Output> {

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
                 UserId userId,
                 String timeZone,
                 Integer page,
                 Integer pageSize) {}

    record Output(List<TransferData> transferInfoList, Long totalPage) { }

}
