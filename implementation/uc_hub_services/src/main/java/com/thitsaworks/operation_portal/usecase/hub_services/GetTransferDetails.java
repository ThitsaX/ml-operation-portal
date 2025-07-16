package com.thitsaworks.operation_portal.usecase.hub_services;

import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;
import com.thitsaworks.operation_portal.core.hub_services.data.TransferDetailData;

public interface GetTransferDetails
    extends UseCase<GetTransferDetails.Input, GetTransferDetails.Output> {

    record Input(String transferId) { }

    record Output(TransferDetailData transferDetailData) { }

}
