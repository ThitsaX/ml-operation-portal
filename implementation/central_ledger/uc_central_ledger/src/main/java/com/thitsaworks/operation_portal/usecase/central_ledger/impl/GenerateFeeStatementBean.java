package com.thitsaworks.operation_portal.usecase.central_ledger.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.report.query.GenerateFeeSettlementRpt;
import com.thitsaworks.operation_portal.usecase.central_ledger.GenerateFeeSettlementReport;
import com.thitsaworks.operation_portal.audit.domain.Auditor;
import com.thitsaworks.operation_portal.audit.exception.UserNotFoundException;
import com.thitsaworks.operation_portal.audit.identity.UserId;
import com.thitsaworks.operation_portal.component.security.SecurityContext;
import com.thitsaworks.operation_portal.component.usecase.UseCaseContext;
import com.thitsaworks.operation_portal.datasource.persistence.CentralLedgerReadTransactional;
import com.thitsaworks.operation_portal.iam.identity.AccessKey;
import com.thitsaworks.operation_portal.iam.query.cache.PrincipalCache;
import com.thitsaworks.operation_portal.iam.query.data.PrincipalData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class GenerateFeeStatementBean extends GenerateFeeSettlementReport {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateFeeStatementBean.class);

    @Autowired
    private GenerateFeeSettlementRpt generateFeeSettlementRpt;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    @Qualifier(PrincipalCache.Strategies.DEFAULT)
    private PrincipalCache principalCache;

    @Override
    @CentralLedgerReadTransactional
    public Output onExecute(Input input) throws Exception {


        GenerateFeeSettlementRpt.Output output = this.generateFeeSettlementRpt.execute(new GenerateFeeSettlementRpt.Input(input.getStartDate(),input.getEndDate(),input.getFromFsp(),input.getToFsp(),input.getCurrency(),input.getTimezone(),input.getFileType()));

        return new Output(output.getRptData());
    }

    @Override
    protected String getName() {

        return GenerateFeeSettlementReport.class.getCanonicalName();

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

        return GenerateFeeSettlementReport.class.getName();

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
                return true;
            case SUPERUSER:
            case ADMIN:
            case REPORTING:
                return false;
        }

        return false;
    }

    @Override

    public void onAudit(GenerateFeeSettlementReport.Input input, GenerateFeeSettlementReport.Output output)
            throws UserNotFoundException {

        SecurityContext securityContext = (SecurityContext) UseCaseContext.get();

        Auditor.audit(this.objectMapper, GenerateFeeSettlementReport.class, input, null,
                new UserId(Long.valueOf(securityContext.getUserId())));
    }

}
