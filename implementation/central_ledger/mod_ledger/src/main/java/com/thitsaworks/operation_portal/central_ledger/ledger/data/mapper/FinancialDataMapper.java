package com.thitsaworks.operation_portal.central_ledger.ledger.data.mapper;

import com.thitsaworks.operation_portal.central_ledger.ledger.data.FinancialData;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FinancialDataMapper implements RowMapper<FinancialData> {

    @Override
    public FinancialData mapRow(ResultSet rs, int rowNum) throws SQLException {

        return new FinancialData(
                rs.getString("dfspName"),
                rs.getString("currency"),
                rs.getBigDecimal("balance"),
                rs.getBigDecimal("currentPosition"),
                rs.getBigDecimal("ndc"),
                rs.getBigDecimal("ndcUsed"));

    }

}
