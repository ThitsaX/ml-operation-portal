package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.reporting.report.domain.data.SettlementIdData;
import com.thitsaworks.operation_portal.reporting.report.query.GetSettlementIdsQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetSettlementId;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class GetSettlementIdHandler extends OperationPortalUseCase<GetSettlementId.Input, GetSettlementId.Output>
    implements GetSettlementId {

    private static final Logger LOG = LoggerFactory.getLogger(GetSettlementIdHandler.class);

    private final GetSettlementIdsQuery getSettlementIdsQuery;

    public GetSettlementIdHandler(PrincipalCache principalCache,
                                  ActionAuthorizationManager actionAuthorizationManager,
                                  GetSettlementIdsQuery getSettlementIdsQuery) {

        super(principalCache, actionAuthorizationManager);

        this.getSettlementIdsQuery = getSettlementIdsQuery;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        GetSettlementIdsQuery.Output output = this.getSettlementIdsQuery.execute(
            new GetSettlementIdsQuery.Input(Timestamp.from(input.startDate()),
                                            Timestamp.from(input.endDate()),
                                            input.timezoneOffset()));

        List<SettlementIdData> settlementIdData = new ArrayList<>();

        for (SettlementIdData data : output.settlementId()) {

            settlementIdData.add(new SettlementIdData(data.getSettlementId()));
        }

        return new GetSettlementId.Output(settlementIdData);
    }

}
