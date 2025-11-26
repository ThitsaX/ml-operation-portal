package com.thitsaworks.operation_portal.core.hub_services.data.mapper;

import com.thitsaworks.operation_portal.core.hub_services.data.SettlementWindowInfoData;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class SettlementWindowInfoDataMapper implements RowMapper<SettlementWindowInfoData> {

    @Override
    public SettlementWindowInfoData mapRow(ResultSet rs, int rowNum) throws SQLException {
        SettlementWindowInfoData.SettlementWindowInfoDataBuilder builder = SettlementWindowInfoData.builder()
                .DfspName(rs.getString("DfspName"))
                .Debit(rs.getBigDecimal("Debit"))
                .Credit(rs.getBigDecimal("Credit"))
                .currencyId(rs.getString("currencyId"))
                .participantSettlementCurrencyId(rs.getString("participantSettlementCurrencyId"))
                .participantLimit(rs.getBigDecimal("participantLimit"))
                .participantBalance(rs.getBigDecimal("participantBalance"))
                .windowOpenedDate(rs.getString("WindowOpenDate"))
                .windowClosedDate(rs.getString("WindowSettledDate"));

        // Safely check if the column exists before trying to access it
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            if ("WindowIDs".equalsIgnoreCase(metaData.getColumnName(i))) {
                builder.settlementWindowIds(rs.getString("WindowIDs"));
                break;
            }
        }

        return builder.build();
    }
}