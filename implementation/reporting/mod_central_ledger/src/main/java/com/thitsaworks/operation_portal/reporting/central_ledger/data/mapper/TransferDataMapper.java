package com.thitsaworks.operation_portal.reporting.central_ledger.data.mapper;

import com.thitsaworks.operation_portal.reporting.central_ledger.data.TransferData;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TransferDataMapper implements RowMapper<TransferData> {

    @Override
    public TransferData mapRow(ResultSet rs, int rowNum) throws SQLException {

        return new TransferData(
                rs.getString("transferId"),
                rs.getString("state"),
                rs.getString("type"),
                rs.getString("currency"),
                rs.getBigDecimal("amount"),
                rs.getString("payer_dfsp"),
                rs.getString("payee_dfsp"),
                rs.getString("window_id"),
                rs.getString("settlement_batch"),
                rs.getString("submitted_on_date")
        );

    }

}
