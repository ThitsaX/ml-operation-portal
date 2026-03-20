package com.thitsaworks.operation_portal.reporting.report.query.impl.jdbc;

import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.thitsaworks.operation_portal.reporting.report.domain.data.SettlementIdData;
import com.thitsaworks.operation_portal.reporting.report.domain.data.mapper.SettlementIdDataMapper;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportErrors;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportException;
import com.thitsaworks.operation_portal.reporting.report.query.GetSettlementIdsWithParentParticipantQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetSettlementIdsWithParentParticipantQueryJdbcHandler implements GetSettlementIdsWithParentParticipantQuery {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GetSettlementIdsWithParentParticipantQueryJdbcHandler(
        @Qualifier(PersistenceQualifiers.Hub.READ_JDBC_TEMPLATE) JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Output execute(Input input) throws ReportException {

        String timezone = input.timezoneOffset();

        List<SettlementIdData> results;
        try {
            results = this.jdbcTemplate.query(
                "SELECT DISTINCT spc.settlementId as settlementId FROM settlementParticipantCurrency spc \n" +
                    " JOIN participantCurrency pc ON pc.participantCurrencyId = spc.participantCurrencyId \n" +
                    " JOIN participant p ON p.participantId = pc.participantId \n" +
                    " LEFT JOIN operation_portal.tbl_participant op \n" +
                    "   ON op.participant_name COLLATE UTF8MB4_UNICODE_CI = p.name COLLATE UTF8MB4_UNICODE_CI \n" +
                    " WHERE ( ? IS NULL \n" +
                    "   OR pc.participantId = ? \n" +
                    "   OR EXISTS ( \n" +
                    "      SELECT 1 \n" +
                    "      FROM participant pFilter \n" +
                    "      LEFT JOIN operation_portal.tbl_participant opFilter \n" +
                    "        ON opFilter.participant_name COLLATE UTF8MB4_UNICODE_CI = pFilter.name COLLATE UTF8MB4_UNICODE_CI \n" +
                    "      WHERE pFilter.participantId = ? \n" +
                    "        AND ( \n" +
                    "          op.participant_name COLLATE UTF8MB4_UNICODE_CI = opFilter.participant_name COLLATE UTF8MB4_UNICODE_CI \n" +
                    "          OR op.parent_participant_name COLLATE UTF8MB4_UNICODE_CI = opFilter.participant_name COLLATE UTF8MB4_UNICODE_CI \n" +
                    "        ) \n" +
                    "   ) \n" +
                    " ) \n" +
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

        return new Output(results);
    }
}
