package com.thitsaworks.operation_portal.core.hub_services.query;


import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesException;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public interface GetNetTransferAmountByWindowIdQuery {

    @Value
    class Input {

        private int settlementWindowId;


    }

    @Value
    class Output {

        private List<WindowInfo> windowInfoList;

        @Value
        public static class WindowInfo implements Serializable {


            private String windowState;

            private String dfspName;

           // private String payeeDfsp;

            private String type;

            private String currency;

            private BigDecimal amount;

            private String windowOpenDate;

            private String windowCloseDate;



        }

    }

    Output execute(Input input) throws HubServicesException;

}
