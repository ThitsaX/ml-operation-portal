package com.thitsaworks.operation_portal.core.hub_services.data.mapper;

import com.thitsaworks.operation_portal.core.hub_services.data.WindowInfoData;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WindowInfoDataMapper implements RowMapper<WindowInfoData> {

    @Override
    public WindowInfoData mapRow(ResultSet rs, int rowNum) throws SQLException {

        return new WindowInfoData(rs.getString("DfspName"),
                                  rs.getBigDecimal("Debit"),
                                  rs.getBigDecimal("Credit"),
                                  rs.getString("currencyId"),
                                  rs.getString("windowOpenedDate"),
                                  rs.getString("windowClosedDate"));
    }

}
