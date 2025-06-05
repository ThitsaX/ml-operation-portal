package com.thitsa.dfsp_portal.ledger.query;

import com.thitsa.dfsp_portal.ledger.data.TransferStateData;
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

    Output execute(Input input) throws Exception;

}
