package com.thitsaworks.operation_portal.usecase.central_ledger.impl;

import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.cache.ParticipantCache;
import com.thitsaworks.operation_portal.core.participant.cache.ParticipantUserCache;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantData;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantUserData;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantErrors;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;
import com.thitsaworks.operation_portal.reporting.central_ledger.query.GetFinancialData;
import com.thitsaworks.operation_portal.usecase.CentralLedgerUseCase;
import com.thitsaworks.operation_portal.usecase.central_ledger.GetDashboardData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class GetDashboardDataHandler extends CentralLedgerUseCase<GetDashboardData.Input, GetDashboardData.Output>
    implements GetDashboardData {

    private static final Logger LOG = LoggerFactory.getLogger(GetDashboardDataHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.OPERATION,
                                                                    UserRoleType.ADMIN);

    private final GetFinancialData getFinancialData;

    private final ParticipantCache participantCache;

    private final ParticipantUserCache participantUserCache;

    public GetDashboardDataHandler(PrincipalCache principalCache,
                                   GetFinancialData getFinancialData,
                                   ParticipantCache participantCache,
                                   ParticipantUserCache participantUserCache) {

        super(PERMITTED_ROLES, principalCache);

        this.getFinancialData = getFinancialData;
        this.participantCache = participantCache;
        this.participantUserCache = participantUserCache;

    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        ParticipantUserData participantUserData = this.participantUserCache.get(input.participantUserId());

        if (participantUserData == null) {

            throw new ParticipantException(ParticipantErrors.USER_NOT_FOUND);
        }

        ParticipantData participantData = this.participantCache.get(participantUserData.participantId());

        if (participantData == null) {

            throw new ParticipantException(ParticipantErrors.PARTICIPANT_NOT_FOUND);
        }

        String
            fspName =
            participantData.dfspCode()
                           .getValue();

        GetFinancialData.Output output = this.getFinancialData.execute(new GetFinancialData.Input(fspName));

        return new GetDashboardData.Output(output.getFinancialData());
    }

}
