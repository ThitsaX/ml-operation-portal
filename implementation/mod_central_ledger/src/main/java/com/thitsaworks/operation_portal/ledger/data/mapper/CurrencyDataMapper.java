package com.thitsaworks.operation_portal.ledger.data.mapper;

import com.thitsaworks.operation_portal.ledger.data.CurrencyData;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CurrencyDataMapper implements RowMapper<CurrencyData> {

    @Override
    public CurrencyData mapRow(ResultSet rs, int rowNum) throws SQLException {

        return new CurrencyData(
                rs.getString("currencyId"));

    }
}
