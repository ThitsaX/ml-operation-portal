package com.thitsaworks.operation_portal.usecase.central_ledger;

import com.thitsaworks.operation_portal.ledger.data.BusinessData;
import com.thitsaworks.operation_portal.component.usecase.AbstractAuditableUseCase;
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
