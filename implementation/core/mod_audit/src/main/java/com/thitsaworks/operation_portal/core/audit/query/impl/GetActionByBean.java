package com.thitsaworks.operation_portal.core.audit.query.impl;

import com.thitsaworks.operation_portal.core.audit.query.GetActionBy;
import com.thitsaworks.operation_portal.core.audit.data.mapper.UserDataMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetActionByBean implements GetActionBy {

    private static final Logger LOG = LoggerFactory.getLogger(GetActionByBean.class);

    private JdbcTemplate dfspJdbcTemplate;

    @Override
    public Output execute(Input input) throws Exception {

        var results = dfspJdbcTemplate.query(
                "SELECT principal_id AS user_id, realm_id AS participant_id FROM tbl_principal WHERE principal_id = ?; \n",
                new UserDataMapper(), input.getUserId().getId());

        if (results == null || results.isEmpty()) {
            return null;
        }

        return new Output(results.get(0));
    }

}
