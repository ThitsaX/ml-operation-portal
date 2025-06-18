package com.thitsaworks.operation_portal.usecase.central_ledger;

import com.thitsaworks.operation_portal.component.misc.usecase.AbstractOwnableUseCase;
import com.thitsaworks.operation_portal.reporting.central_ledger.data.TransferStateData;
import java.io.Serializable;
import java.util.List;

public abstract class GetAllTransferState
        extends AbstractOwnableUseCase<GetAllTransferState.Input, GetAllTransferState.Output> {

    public record Input() {}

    public record Output(List<TransferStateData> transferStateDataList) {
        public record TransferStateInfo(String transferState, String transferStateId) implements Serializable {}
    }
}
