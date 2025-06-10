package com.thitsaworks.operation_portal.audit.query.data.mapper;

import com.thitsaworks.operation_portal.audit.query.data.UserData;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDataMapper implements RowMapper<UserData> {

    @Override
    public UserData mapRow(ResultSet rs, int rowNum) throws SQLException {

        return new UserData(rs.getLong("user_id"), rs.getLong("participant_id"));

    }

}
