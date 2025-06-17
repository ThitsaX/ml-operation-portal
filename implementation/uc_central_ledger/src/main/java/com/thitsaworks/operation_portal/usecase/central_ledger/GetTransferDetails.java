package com.thitsaworks.operation_portal.usecase.central_ledger;

import com.thitsaworks.operation_portal.component.misc.usecase.AbstractAuditableUseCase;
import com.thitsaworks.operation_portal.reporting.central_ledger.data.BusinessData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public abstract class GetTransferDetails
        extends AbstractAuditableUseCase<GetTransferDetails.Input, GetTransferDetails.Output> {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Input {

        private String transferId;

    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Output {

        private BusinessData businessData;

    }

}
