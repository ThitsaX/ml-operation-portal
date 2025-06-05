package com.thitsa.dfsp_portal.ledger.data.mapper;

import com.thitsa.dfsp_portal.ledger.data.BusinessData;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BusinessDataMapper implements RowMapper<BusinessData> {

    @Override
    public BusinessData mapRow(ResultSet rs, int rowNum) throws SQLException {

        return new BusinessData(
                rs.getString("transferId"),
                rs.getString("state"),
                rs.getString("type"),
                rs.getString("currency"),
                rs.getBigDecimal("amount"),
                rs.getString("payer"),
                rs.getString("payer_details"),
                rs.getString("payer_dfsp"),
                rs.getString("payee"),
                rs.getString("payee_details"),
                rs.getString("payee_dfsp"),
                rs.getString("settlement_batch"),
                rs.getString("submitted_on_date")
        );

    }

}
