package com.thitsaworks.operation_portal.usecase.central_ledger.impl;

import com.thitsaworks.operation_portal.ledger.query.GetFinancialData;
import com.thitsaworks.operation_portal.usecase.central_ledger.GetDashboardData;
import com.thitsaworks.operation_portal.component.security.SecurityContext;
import com.thitsaworks.operation_portal.datasource.persistence.CentralLedgerReadTransactional;
import com.thitsaworks.operation_portal.iam.identity.AccessKey;
import com.thitsaworks.operation_portal.iam.query.cache.PrincipalCache;
import com.thitsaworks.operation_portal.iam.query.data.PrincipalData;
import com.thitsaworks.operation_portal.participant.exception.ParticipantNotFoundException;
import com.thitsaworks.operation_portal.participant.exception.ParticipantUserNotFoundException;
import com.thitsaworks.operation_portal.participant.query.cache.ParticipantCache;
import com.thitsaworks.operation_portal.participant.query.cache.ParticipantUserCache;
import com.thitsaworks.operation_portal.participant.query.data.ParticipantData;
import com.thitsaworks.operation_portal.participant.query.data.ParticipantUserData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class GetDashboardDataBean extends GetDashboardData {

    private static final Logger LOG = LoggerFactory.getLogger(GetDashboardDataBean.class);

    @Autowired
    private GetFinancialData getFinancialData;

    @Autowired
    @Qualifier(ParticipantCache.Strategies.DEFAULT)
    private ParticipantCache participantCache;

    @Autowired
    @Qualifier(ParticipantUserCache.Strategies.DEFAULT)
    private ParticipantUserCache participantUserCache;

    @Autowired
    @Qualifier(PrincipalCache.Strategies.DEFAULT)
    private PrincipalCache principalCache;

    @Override
    @CentralLedgerReadTransactional
    public GetDashboardData.Output onExecute(GetDashboardData.Input input)
            throws ParticipantUserNotFoundException, ParticipantNotFoundException {

        ParticipantUserData participantUserData = this.participantUserCache.get(input.getParticipantUserId());

        if (participantUserData == null) {

            throw new ParticipantUserNotFoundException(input.getParticipantUserId().getId().toString());
        }

        ParticipantData participantData = this.participantCache.get(participantUserData.getParticipantId());

        if (participantData == null) {

            throw new ParticipantNotFoundException(participantUserData.getParticipantId().getId().toString());
        }

        String fspName = participantData.getDfspCode().getValue();

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

        switch (principalData.getUserRoleType()) {

            case OPERATION:
            case ADMIN:
                return true;
            case SUPERUSER:
            case REPORTING:
                return false;
        }

        return false;
    }
}
