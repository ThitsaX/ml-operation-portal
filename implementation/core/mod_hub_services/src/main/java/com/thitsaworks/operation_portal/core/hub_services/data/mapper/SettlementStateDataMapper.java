package com.thitsaworks.operation_portal.core.hub_services.data.mapper;

import com.thitsaworks.operation_portal.core.hub_services.data.SettlementStateData;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SettlementStateDataMapper implements RowMapper<SettlementStateData> {

    @Override
    public SettlementStateData mapRow(ResultSet rs, int rowNum) throws SQLException {

        return new SettlementStateData(

                rs.getString("settlementStateId"),
                rs.getString("enumeration")

        );
    }

}
