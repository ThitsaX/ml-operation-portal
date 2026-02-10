package com.thitsaworks.operation_portal.core.hub_services.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SettlementWindowInfoData implements Serializable {
    private String DfspName;
    private BigDecimal Debit;
    private BigDecimal Credit;
    private String currencyId;
    private String windowOpenedDate;
    private String participantSettlementCurrencyId;
    private BigDecimal participantLimit;
    private BigDecimal participantBalance;
    private BigDecimal ndcPercent;
    private String windowClosedDate;
    private String settlementWindowIds;  // Optional field, can be null
}