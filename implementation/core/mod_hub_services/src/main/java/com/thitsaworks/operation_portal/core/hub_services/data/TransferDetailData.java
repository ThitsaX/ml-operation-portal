package com.thitsaworks.operation_portal.core.hub_services.data;

public record TransferDetailData(

        String transferId,

        String quoteId,

        String transferState,

        String transferType,

        String subScenario,

        String currency,

        String amountType,

        String quoteAmount,

        String transferAmount,

        String payeeReceivedAmount,

        String payeeDfspFee,

        String payeeDfspCommission,

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


