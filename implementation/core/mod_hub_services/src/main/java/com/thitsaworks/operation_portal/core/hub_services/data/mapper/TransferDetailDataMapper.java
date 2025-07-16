package com.thitsaworks.operation_portal.core.hub_services.data.mapper;

import com.thitsaworks.operation_portal.core.hub_services.data.TransferDetailData;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TransferDetailDataMapper implements RowMapper<TransferDetailData> {

    @Override
    public TransferDetailData mapRow(ResultSet rs, int rowNum) throws SQLException {

        TransferDetailData.PartyInfoData payerInformation = new TransferDetailData.PartyInfoData(
                rs.getString("payerIdType"),
                rs.getString("payerIdValue"),
                rs.getString("payerDspId"),
                rs.getString("payerName")
        );

        TransferDetailData.PartyInfoData payeeInformation = new TransferDetailData.PartyInfoData(
                rs.getString("payeeIdType"),
                rs.getString("payeeIdValue"),
                rs.getString("payeeDspId"),
                rs.getString("payeeName")
        );

        TransferDetailData.TransferErrorInfo errorInfo = new TransferDetailData.TransferErrorInfo(
                rs.getString("errorCode"),
                rs.getString("errorDescription")
        );

        return new TransferDetailData(
                rs.getString("transferId"),
                rs.getString("quoteId"),
                rs.getString("transferState"),
                rs.getString("transferType"),
                rs.getString("currency"),
                rs.getString("amountType"),
                rs.getBigDecimal("quoteAmount"),
                rs.getBigDecimal("transferAmount"),
                rs.getBigDecimal("payeeReceivedAmount"),
                rs.getBigDecimal("payeeDfspFeeAmount"),
                rs.getBigDecimal("payeeDfspCommissionAmount"),
                rs.getString("submitted_on_date"),
                rs.getString("windowId"),
                rs.getString("settlementId"),
                payerInformation,
                payeeInformation,
                errorInfo
        );

    }

}
