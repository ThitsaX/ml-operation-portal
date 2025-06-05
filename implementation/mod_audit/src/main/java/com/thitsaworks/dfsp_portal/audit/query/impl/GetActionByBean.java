package com.thitsaworks.dfsp_portal.audit.query.impl;

import com.thitsaworks.dfsp_portal.audit.query.GetActionBy;
import com.thitsaworks.dfsp_portal.audit.query.data.mapper.UserDataMapper;
import com.thitsaworks.dfsp_portal.datasource.persistence.ReadTransactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@ReadTransactional
public class GetActionByBean implements GetActionBy {

    private static final Logger LOG = LoggerFactory.getLogger(GetActionByBean.class);

    @Autowired
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
