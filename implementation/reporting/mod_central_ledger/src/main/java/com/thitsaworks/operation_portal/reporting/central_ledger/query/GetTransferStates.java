package com.thitsaworks.operation_portal.reporting.central_ledger.query;

import com.thitsaworks.operation_portal.reporting.central_ledger.data.TransferStateData;
import com.thitsaworks.operation_portal.reporting.central_ledger.exception.CentralLedgerException;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

public interface GetTransferStates {

    @Value
    class Input {

    }

    @Value
    class Output {

        private List<TransferStateData> transferStateDataList;

        @Value
        public static class TransferStateInfo implements Serializable {

            private String transferState;

            private String transferStateId;

        }

    }

    Output execute(Input input) throws CentralLedgerException;

}
