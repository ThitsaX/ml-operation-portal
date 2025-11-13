package com.thitsaworks.operation_portal.core.hub_services.data.mapper;

import com.thitsaworks.operation_portal.core.hub_services.data.ParticipantBalanceData;
import com.thitsaworks.operation_portal.core.hub_services.data.ParticipantLimitData;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ParticipantLimitDataMapper implements RowMapper<ParticipantLimitData> {

    @Override
    public ParticipantLimitData mapRow(ResultSet rs, int rowNum) throws SQLException {

        return new ParticipantLimitData(
            rs.getString("participantCurrencyId"),

            rs.getBigDecimal("value"),

            rs.getBoolean("isActive")

        );
    }

}
