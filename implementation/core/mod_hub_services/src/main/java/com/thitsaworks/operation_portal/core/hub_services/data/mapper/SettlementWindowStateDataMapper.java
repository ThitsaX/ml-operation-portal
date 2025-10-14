package com.thitsaworks.operation_portal.core.hub_services.data.mapper;

import com.thitsaworks.operation_portal.core.hub_services.data.SettlementWindowStateData;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SettlementWindowStateDataMapper implements RowMapper<SettlementWindowStateData> {

    @Override
    public SettlementWindowStateData mapRow(ResultSet rs, int rowNum) throws SQLException {

        return new SettlementWindowStateData(
                rs.getString("settlementWindowStateId"),
                rs.getString("enumeration")
        );
    }

}
