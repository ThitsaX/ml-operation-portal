package com.thitsaworks.operation_portal.usecase.central_ledger.impl;

import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.reporting.report.domain.data.SettlementIdData;
import com.thitsaworks.operation_portal.reporting.report.query.GetSettlementIds;
import com.thitsaworks.operation_portal.usecase.CentralLedgerUseCase;
import com.thitsaworks.operation_portal.usecase.central_ledger.GetSettlementId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@Service
public class GetSettlementIdHandler extends CentralLedgerUseCase<GetSettlementId.Input, GetSettlementId.Output>
    implements GetSettlementId {

    private static final Logger LOG = LoggerFactory.getLogger(GetSettlementIdHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = EnumSet.allOf(UserRoleType.class);

    private final GetSettlementIds getSettlementIds;

    public GetSettlementIdHandler(PrincipalCache principalCache,
                                  GetSettlementIds getSettlementIds) {

        super(PERMITTED_ROLES, principalCache);

        this.getSettlementIds = getSettlementIds;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        GetSettlementIds.Output output = this.getSettlementIds.execute(
            new GetSettlementIds.Input(Timestamp.from(input.startDate()),
                                       Timestamp.from(input.endDate()),
                                       input.timezoneOffset()));

        List<SettlementIdData> settlementIdData = new ArrayList<>();

        for (SettlementIdData data : output.settlementId()) {

            settlementIdData.add(new SettlementIdData(data.getSettlementId()));
        }

        return new GetSettlementId.Output(settlementIdData);
    }

}
