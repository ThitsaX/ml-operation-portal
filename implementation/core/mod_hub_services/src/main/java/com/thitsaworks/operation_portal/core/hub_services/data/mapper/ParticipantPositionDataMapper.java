package com.thitsaworks.operation_portal.core.hub_services.data.mapper;

import com.thitsaworks.operation_portal.core.hub_services.data.FinancialData;
import com.thitsaworks.operation_portal.core.hub_services.data.ParticipantPositionData;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ParticipantPositionDataMapper implements RowMapper<ParticipantPositionData> {

    @Override
    public ParticipantPositionData mapRow(ResultSet rs, int rowNum) throws SQLException {

        return new ParticipantPositionData(
                rs.getString("dfspId"),
                rs.getString("dfspName"),
                rs.getString("currency"),
                rs.getInt("participantSettlementCurrencyId"),
                rs.getInt("participantPositionCurrencyID"),
                rs.getBoolean("isActive"));

    }

}
