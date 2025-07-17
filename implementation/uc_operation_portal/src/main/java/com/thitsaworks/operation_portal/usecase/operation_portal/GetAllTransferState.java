package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;
import com.thitsaworks.operation_portal.core.hub_services.data.TransferStateData;

import java.io.Serializable;
import java.util.List;

public interface GetAllTransferState
    extends UseCase<GetAllTransferState.Input, GetAllTransferState.Output> {

    record Input() { }

    record Output(List<TransferStateData> transferStateDataList) {

        public record TransferStateInfo(String transferState,
                                        String transferStateId) implements Serializable { }

    }

}
