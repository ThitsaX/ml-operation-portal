package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;
import com.thitsaworks.operation_portal.core.hub_services.data.TransferDetailData;

public interface GetTransferDetail
    extends UseCase<GetTransferDetail.Input, GetTransferDetail.Output> {

    record Input(String transferId) { }

    record Output(TransferDetailData transferDetailData) { }

}
