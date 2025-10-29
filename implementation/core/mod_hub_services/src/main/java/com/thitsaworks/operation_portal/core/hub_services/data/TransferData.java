package com.thitsaworks.operation_portal.core.hub_services.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TransferData implements Serializable {

    private String transferId;

    private String state;

    private String type;

    private String currency;

    private BigDecimal amount;

    private String payerDfsp;

    private String payerDfspName;

    private String payeeDfsp;

    private String payeeDfspName;

    private String windowId;

    private String settlementBatch;

    private String submittedOnDate;

}
