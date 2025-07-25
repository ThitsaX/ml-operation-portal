package com.thitsaworks.operation_portal.core.hub_services.data.mapper;

import com.thitsaworks.operation_portal.core.hub_services.data.FinancialData;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FinancialDataMapper implements RowMapper<FinancialData> {

    @Override
    public FinancialData mapRow(ResultSet rs, int rowNum) throws SQLException {

        return new FinancialData(
                rs.getString("dfspId"),
                rs.getString("dfspName"),
                rs.getString("currency"),
                rs.getBigDecimal("balance"),
                rs.getBigDecimal("currentPosition"),
                rs.getBigDecimal("ndcPercent"),
                rs.getBigDecimal("ndc"),
                rs.getBigDecimal("ndcUsed"),
                rs.getInt("participantSettlementCurrencyId"),
                rs.getInt("participantPositionCurrencyID"));

    }

}
