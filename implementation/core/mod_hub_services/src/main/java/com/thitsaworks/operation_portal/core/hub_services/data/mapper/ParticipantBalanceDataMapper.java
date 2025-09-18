package com.thitsaworks.operation_portal.core.hub_services.data.mapper;

import com.thitsaworks.operation_portal.core.hub_services.data.ParticipantBalanceData;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ParticipantBalanceDataMapper implements RowMapper<ParticipantBalanceData> {

    @Override
    public ParticipantBalanceData mapRow(ResultSet rs, int rowNum) throws SQLException {

        return new ParticipantBalanceData(
                rs.getString("currency"),
                rs.getString("ledgerAccountType"),
                rs.getBigDecimal("value"),
                rs.getBigDecimal("reservedValue"),
                rs.getBoolean("isActive"),
                rs.getTimestamp("changedDate").toInstant()
        );
    }

}
