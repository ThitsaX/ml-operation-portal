package com.thitsaworks.operation_portal.core.hub_services.data.mapper;

import com.thitsaworks.operation_portal.core.hub_services.data.WindowInfoData;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class WindowInfoDataMapper implements RowMapper<WindowInfoData> {

    @Override
    public WindowInfoData mapRow(ResultSet rs, int rowNum) throws SQLException {

        WindowInfoData.WindowInfoDataBuilder
            builder =
            WindowInfoData.builder()
                          .DfspName(rs.getString("DfspName"))
                          .Debit(rs.getBigDecimal("Debit"))
                          .Credit(rs.getBigDecimal("Credit"))
                          .currencyId(rs.getString("currencyId"))
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