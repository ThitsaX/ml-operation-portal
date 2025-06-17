package com.thitsaworks.operation_portal.usecase.central_ledger.impl;

import com.thitsaworks.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.security.SecurityContext;
import com.thitsaworks.operation_portal.core.iam.query.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.query.data.PrincipalData;
import com.thitsaworks.operation_portal.core.participant.cache.ParticipantCache;
import com.thitsaworks.operation_portal.core.participant.cache.ParticipantUserCache;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantData;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantUserData;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantNotFoundException;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantUserNotFoundException;
import com.thitsaworks.operation_portal.reporting.central_ledger.query.GetFinancialData;
import com.thitsaworks.operation_portal.usecase.central_ledger.GetDashboardData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetDashboardDataHandler extends GetDashboardData {

    private static final Logger LOG = LoggerFactory.getLogger(GetDashboardDataHandler.class);

    private GetFinancialData getFinancialData;

    private ParticipantCache participantCache;

    private ParticipantUserCache participantUserCache;

    private PrincipalCache principalCache;

    @Autowired
    public GetDashboardDataHandler(GetFinancialData getFinancialData,
                                   ParticipantCache participantCache,
                                   ParticipantUserCache participantUserCache,
                                   PrincipalCache principalCache) {

        this.getFinancialData = getFinancialData;
        this.participantCache = participantCache;
        this.participantUserCache = participantUserCache;
        this.principalCache = principalCache;
    }

    @Override
    public GetDashboardData.Output onExecute(GetDashboardData.Input input)
            throws ParticipantUserNotFoundException, ParticipantNotFoundException {

        ParticipantUserData participantUserData = this.participantUserCache.get(input.getParticipantUserId());

        if (participantUserData == null) {

            throw new ParticipantUserNotFoundException(input.getParticipantUserId().getId().toString());
        }

        ParticipantData participantData = this.participantCache.get(participantUserData.participantId());

        if (participantData == null) {

            throw new ParticipantNotFoundException(participantUserData.participantUserId().getId().toString());
        }

        String fspName = participantData.dfspCode().getValue();

        GetFinancialData.Output output = this.getFinancialData.execute(new GetFinancialData.Input(fspName));

        return new GetDashboardData.Output(output.getFinancialData());
    }

    @Override
    protected String getName() {

        return GetDashboardData.class.getCanonicalName();
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

        return GetDashboardData.class.getName();
    }

    @Override
    public boolean isOwned(Object userDetails) {

        return true;
    }

    @Override
    public boolean isAuthorized(Object userDetails) {

        SecurityContext securityContext = (SecurityContext) userDetails;

        PrincipalData principalData =
                this.principalCache.get(new AccessKey(Long.parseLong(securityContext.getAccessKey())));

        return switch (principalData.getUserRoleType()) {
            case OPERATION, ADMIN -> true;
            case SUPERUSER, REPORTING -> false;
        };

    }
}
