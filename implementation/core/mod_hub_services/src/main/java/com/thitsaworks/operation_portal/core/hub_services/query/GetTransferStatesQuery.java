package com.thitsaworks.operation_portal.core.hub_services.query;

import com.thitsaworks.operation_portal.core.hub_services.data.TransferStateData;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesException;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

public interface GetTransferStatesQuery {

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

    Output execute(Input input) throws HubServicesException;

}
