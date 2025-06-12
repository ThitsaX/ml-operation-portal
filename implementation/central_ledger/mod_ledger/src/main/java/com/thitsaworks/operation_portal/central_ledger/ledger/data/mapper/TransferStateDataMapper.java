package com.thitsaworks.operation_portal.central_ledger.ledger.data.mapper;

import com.thitsaworks.operation_portal.central_ledger.ledger.data.TransferStateData;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TransferStateDataMapper implements RowMapper<TransferStateData> {

    @Override
    public TransferStateData mapRow(ResultSet rs, int rowNum) throws SQLException {

        return new TransferStateData(
                rs.getString("enumeration"),
                rs.getString("enumeration"));

    }

}
