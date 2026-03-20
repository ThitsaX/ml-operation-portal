package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.reporting.report.domain.data.SettlementIdData;
import com.thitsaworks.operation_portal.reporting.report.query.GetSettlementIdsWithParentParticipantQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetSettlementIdWithParentParticipant;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class GetSettlementIdWithParentParticipantHandler
        extends OperationPortalUseCase<GetSettlementIdWithParentParticipant.Input, GetSettlementIdWithParentParticipant.Output>
        implements GetSettlementIdWithParentParticipant {

    private final GetSettlementIdsWithParentParticipantQuery getSettlementIdsWithParentParticipantQuery;

    public GetSettlementIdWithParentParticipantHandler(PrincipalCache principalCache,
                                                       ActionAuthorizationManager actionAuthorizationManager,
                                                       GetSettlementIdsWithParentParticipantQuery getSettlementIdsWithParentParticipantQuery) {
        super(principalCache, actionAuthorizationManager);
        this.getSettlementIdsWithParentParticipantQuery = getSettlementIdsWithParentParticipantQuery;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        GetSettlementIdsWithParentParticipantQuery.Output output = this.getSettlementIdsWithParentParticipantQuery.execute(
                new GetSettlementIdsWithParentParticipantQuery.Input(Timestamp.from(input.startDate()),
                                                                     Timestamp.from(input.endDate()),
                                                                     input.dfspId(),
                                                                     input.timezoneOffset()));

        List<SettlementIdData> settlementIdData = new ArrayList<>();
        for (SettlementIdData data : output.settlementId()) {
            settlementIdData.add(new SettlementIdData(data.getSettlementId()));
        }

        return new Output(settlementIdData);
    }
}
