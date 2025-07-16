package com.thitsaworks.operation_portal.core.hub_services.data;

import java.math.BigDecimal;
import java.util.List;

public record TransferDetailData(

        String transferId,

        String quoteId,

        String transferState,

        String transferType,

        String currency,

        String amountType,

        BigDecimal quoteAmount,

        BigDecimal transferAmount,

        BigDecimal payeeReceivedAmount,

        BigDecimal payeeDfspFee,

        BigDecimal payeeDfspCommission,

        String submittedOnDate,

        String windowId,

        String settlementId,

        PartyInfoData payerInformation,

        PartyInfoData payeeInformation,

        TransferErrorInfo transferErrorInfo
) {

    public record PartyInfoData(

            String idType,

            String idValue,

            String dfspId,

            String name) {}

    public record TransferErrorInfo(

            String errorCode,

            String errorDescription) {}

}


