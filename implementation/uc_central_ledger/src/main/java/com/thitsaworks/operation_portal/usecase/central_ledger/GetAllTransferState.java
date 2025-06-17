package com.thitsaworks.operation_portal.usecase.central_ledger;

import com.thitsaworks.operation_portal.component.misc.usecase.AbstractOwnableUseCase;
import com.thitsaworks.operation_portal.reporting.central_ledger.data.TransferStateData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

public abstract class GetAllTransferState
        extends AbstractOwnableUseCase<GetAllTransferState.Input, GetAllTransferState.Output> {

    @Getter
    @NoArgsConstructor
    public static class Input {

    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Output {

        private List<TransferStateData> transferStateDataList;

        @Value
        public static class TransferStateInfo implements Serializable {

            private String transferState;

            private String transferStateId;

        }

    }

}
