package com.thitsaworks.operation_portal.report.query.impl;

import com.thitsaworks.operation_portal.datasource.persistence.ReadTransactional;
import com.thitsaworks.operation_portal.report.domain.data.mapper.SettlementIdDataMapper;
import com.thitsaworks.operation_portal.report.query.GetSettlementIds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@ReadTransactional
public class GetSettlementIdsBean implements GetSettlementIds {

    private static final Logger LOG = LoggerFactory.getLogger(GetSettlementIdsBean.class);

    @Autowired
    private JdbcTemplate centralLedgerJdbcTemplate;

    @Override
    public Output execute(Input input) {

        String timezone=input.getTimezoneOffset();

        var results = centralLedgerJdbcTemplate.query(
                "SELECT settlementId FROM settlement WHERE createdDate BETWEEN \n" +
                        " (CASE WHEN SUBSTRING(?,1,1) = '-' THEN \n" +
                        " CONVERT_TZ(? ,CONCAT(SUBSTRING(?,1,3),':',SUBSTRING(?,4,2)),'+00:00') ELSE \n" +
                        " CONVERT_TZ(?,CONCAT('+',SUBSTRING(?,1,2),':',SUBSTRING(?,3,2)) ,'+00:00') END) \n" +
                        " AND \n" +
                        " (CASE WHEN SUBSTRING(?,1,1) = '-' THEN \n" +
                        " CONVERT_TZ(? ,CONCAT(SUBSTRING(?,1,3),':',SUBSTRING(?,4,2)),'+00:00') ELSE\n" +
                        " CONVERT_TZ(?,CONCAT('+',SUBSTRING(?,1,2),':',SUBSTRING(?,3,2)) ,'+00:00') END) ;",
                new SettlementIdDataMapper(),
                timezone,
                input.getStartDate(),timezone,timezone,
                input.getStartDate(),timezone,timezone,
                timezone,
                input.getEndDate(),timezone,timezone,
                input.getEndDate(),timezone,timezone);

        if (results == null || results.isEmpty()) {

            return new Output(new ArrayList<>());
        }

        return new GetSettlementIds.Output(results);
    }
}
