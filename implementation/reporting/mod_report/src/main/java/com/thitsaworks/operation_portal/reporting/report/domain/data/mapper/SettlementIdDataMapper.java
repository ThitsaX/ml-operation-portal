package com.thitsaworks.operation_portal.reporting.report.domain.data.mapper;

import com.thitsaworks.operation_portal.reporting.report.domain.data.SettlementIdData;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SettlementIdDataMapper implements RowMapper<SettlementIdData> {

    @Override
    public SettlementIdData mapRow(ResultSet rs, int rowNum) throws SQLException {

        return new SettlementIdData(
                rs.getString("settlementId")
        );

    }

}
