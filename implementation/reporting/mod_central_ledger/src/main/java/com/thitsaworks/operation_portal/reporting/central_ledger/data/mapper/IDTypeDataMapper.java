package com.thitsaworks.operation_portal.reporting.central_ledger.data.mapper;

import com.thitsaworks.operation_portal.reporting.central_ledger.data.IDTypeData;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class IDTypeDataMapper implements RowMapper<IDTypeData> {

    @Override
    public IDTypeData mapRow(ResultSet rs, int rowNum) throws SQLException {

        return new IDTypeData(
                rs.getString("partyIdentifierTypeId"),
                rs.getString("name"));

    }

}
