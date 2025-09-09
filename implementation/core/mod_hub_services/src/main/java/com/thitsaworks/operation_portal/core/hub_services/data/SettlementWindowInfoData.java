package com.thitsaworks.operation_portal.core.hub_services.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SettlementWindowInfoData implements Serializable {
    private String DfspName;
    private BigDecimal Debit;
    private BigDecimal Credit;
    private String currencyId;
    private String windowOpenedDate;
    private String windowClosedDate;
    private String settlementWindowIds;  // Optional field, can be null
}