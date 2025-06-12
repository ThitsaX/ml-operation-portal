package com.thitsaworks.operation_portal.central_ledger.report.query;

import com.thitsaworks.operation_portal.central_ledger.report.domain.data.SettlementIdData;
import lombok.Value;

import java.sql.Timestamp;
import java.util.List;

public interface GetSettlementIds {

    @Value
    class Input {

        private Timestamp startDate;

        private Timestamp endDate;

        private String timezoneOffset ;

    }

    @Value
    class Output {

        private List<SettlementIdData> settlementId;

    }

    Output execute(Input input) throws Exception;

}
