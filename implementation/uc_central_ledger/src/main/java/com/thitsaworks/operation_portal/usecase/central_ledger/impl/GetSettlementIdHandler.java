package com.thitsaworks.operation_portal.usecase.central_ledger.impl;

import com.thitsaworks.operation_portal.reporting.report.domain.data.SettlementIdData;
import com.thitsaworks.operation_portal.reporting.report.query.GetSettlementIds;
import com.thitsaworks.operation_portal.usecase.central_ledger.GetSettlementId;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetSettlementIdHandler extends GetSettlementId {

    private static final Logger LOG = LoggerFactory.getLogger(GetSettlementIdHandler.class);

    private final GetSettlementIds getSettlementIds;

    @Override
    public Output onExecute(Input input) throws Exception {

        GetSettlementIds.Output output = this.getSettlementIds.execute(
                new GetSettlementIds.Input(Timestamp.from(input.startDate()), Timestamp.from(input.endDate()),input.timezoneOffset()));

        List<SettlementIdData> settlementIdData = new ArrayList<>();

        for (SettlementIdData data : output.getSettlementId()) {

            settlementIdData.add(new SettlementIdData(data.getSettlementId()));
        }

        return new GetSettlementId.Output(settlementIdData);
    }

    @Override
    protected String getName() {

        return GetSettlementId.class.getCanonicalName();
    }

    @Override
    protected String getDescription() {

        return null;
    }

    @Override
    protected String getScope() {

        return "uc_central_ledger";
    }

    @Override
    protected String getId() {

        return GetSettlementId.class.getName();
    }

    @Override
    public boolean isOwned(Object userDetails) {

        return true;
    }

    @Override
    public boolean isAuthorized(Object userDetails) {

        return true;
    }

}
