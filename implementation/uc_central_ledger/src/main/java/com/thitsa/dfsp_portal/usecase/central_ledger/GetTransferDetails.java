package com.thitsa.dfsp_portal.usecase.central_ledger;

import com.thitsa.dfsp_portal.ledger.data.BusinessData;
import com.thitsaworks.dfsp_portal.component.usecase.AbstractAuditableUseCase;
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
