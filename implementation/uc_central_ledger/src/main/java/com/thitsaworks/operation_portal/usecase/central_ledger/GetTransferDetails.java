package com.thitsaworks.operation_portal.usecase.central_ledger;

import com.thitsaworks.operation_portal.component.misc.usecase.AbstractAuditableUseCase;
import com.thitsaworks.operation_portal.reporting.central_ledger.data.BusinessData;

public abstract class GetTransferDetails
        extends AbstractAuditableUseCase<GetTransferDetails.Input, GetTransferDetails.Output> {

    public record Input(String transferId) {}

    public record Output(BusinessData businessData) {}
}
