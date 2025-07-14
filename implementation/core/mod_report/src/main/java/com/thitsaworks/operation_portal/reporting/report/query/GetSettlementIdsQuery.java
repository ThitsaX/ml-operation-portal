package com.thitsaworks.operation_portal.reporting.report.query;

import com.thitsaworks.operation_portal.reporting.report.domain.data.SettlementIdData;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportException;

import java.sql.Timestamp;
import java.util.List;

public interface GetSettlementIdsQuery {

    record Input(
            Timestamp startDate,
            Timestamp endDate,
            String timezoneOffset
    ) {}

    record Output(
            List<SettlementIdData> settlementId
    ) {}

    Output execute(Input input) throws ReportException;

}
