package com.thitsaworks.operation_portal.usecase.central_ledger;

import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;
import com.thitsaworks.operation_portal.reporting.central_ledger.data.BusinessData;

public interface GetTransferDetails
    extends UseCase<GetTransferDetails.Input, GetTransferDetails.Output> {

    record Input(String transferId) { }

    record Output(BusinessData businessData) { }

}
