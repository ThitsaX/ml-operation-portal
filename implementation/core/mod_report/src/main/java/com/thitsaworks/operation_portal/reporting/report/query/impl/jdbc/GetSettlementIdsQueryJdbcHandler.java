package com.thitsaworks.operation_portal.reporting.report.query.impl.jdbc;

import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.thitsaworks.operation_portal.reporting.report.domain.data.SettlementIdData;
import com.thitsaworks.operation_portal.reporting.report.domain.data.mapper.SettlementIdDataMapper;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportErrors;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportException;
import com.thitsaworks.operation_portal.reporting.report.query.GetSettlementIdsQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetSettlementIdsQueryJdbcHandler implements GetSettlementIdsQuery {

    private static final Logger LOG = LoggerFactory.getLogger(GetSettlementIdsQueryJdbcHandler.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GetSettlementIdsQueryJdbcHandler(
        @Qualifier(PersistenceQualifiers.Hub.READ_JDBC_TEMPLATE) JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Output execute(Input input) throws ReportException {

        String timezone = input.timezoneOffset();

        List<SettlementIdData> results;
        try {

            results = this.jdbcTemplate.query(
                "SELECT spc.settlementId as settlementId FROM settlementParticipantCurrency spc \n" +
                    " JOIN participantCurrency pc ON pc.participantCurrencyId = spc.participantCurrencyId \n" +
                    " WHERE (? IS NULL OR pc.participantId = ?) \n" +
                    " AND spc.createdDate BETWEEN \n" +
                    " (CASE WHEN SUBSTRING(?,1,1) = '-' THEN \n" +
                    "   CONVERT_TZ(? ,CONCAT(SUBSTRING(?,1,3),':',SUBSTRING(?,4,2)),'+00:00') ELSE \n" +
                    "   CONVERT_TZ(?,CONCAT('+',SUBSTRING(?,1,2),':',SUBSTRING(?,3,2)) ,'+00:00') END) \n" +
                    " AND \n" +
                    " (CASE WHEN SUBSTRING(?,1,1) = '-' THEN \n" +
                    "   CONVERT_TZ(? ,CONCAT(SUBSTRING(?,1,3),':',SUBSTRING(?,4,2)),'+00:00') ELSE\n" +
                    "   CONVERT_TZ(?,CONCAT('+',SUBSTRING(?,1,2),':',SUBSTRING(?,3,2)) ,'+00:00') END)",
                new SettlementIdDataMapper(),
                input.dfspId(),
                input.dfspId(),
                timezone,
                input.startDate(), timezone, timezone,
                input.startDate(), timezone, timezone,
                timezone,
                input.endDate(), timezone, timezone,
                input.endDate(), timezone, timezone);

        } catch (Exception e) {
            throw new ReportException(ReportErrors.REPORT_FAILURE_EXCEPTION);
        }

        if (results == null || results.isEmpty()) {
            return new Output(new ArrayList<>());
        }

        return new GetSettlementIdsQuery.Output(results);
    }

}
