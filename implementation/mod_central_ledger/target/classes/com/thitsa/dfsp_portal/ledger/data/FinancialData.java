package com.thitsa.dfsp_portal.ledger.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FinancialData implements Serializable {

    private String dfspName;

    private String currency;

    private BigDecimal balance;

    private BigDecimal currentPosition;

    private BigDecimal ndc;

    private BigDecimal ndcUsed;

}
