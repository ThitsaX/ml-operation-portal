package com.thitsaworks.operation_portal.usecase.central_ledger.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.report.query.GenerateDetailRpt;
import com.thitsaworks.operation_portal.usecase.central_ledger.GenerateDetailReport;
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
public class GenerateDetailReportBean extends GenerateDetailReport {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateDetailReportBean.class);

    @Autowired
    private GenerateDetailRpt generateDetailRpt;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    @Qualifier(PrincipalCache.Strategies.DEFAULT)
    private PrincipalCache principalCache;

    @Override
    @CentralLedgerReadTransactional
    public Output onExecute(Input input) throws Exception {

        GenerateDetailRpt.Output output = this.generateDetailRpt.execute(
                new GenerateDetailRpt.Input(input.getSettlementId(), input.getFspId(), input.getFileType(),input.getTimezoneOffset()));

        return new Output(output.getDetailReportByte());

    }

    @Override
    protected String getName() {

        return GenerateDetailReport.class.getCanonicalName();
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

        return GenerateDetailReport.class.getName();
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
    public void onAudit(GenerateDetailReport.Input input, GenerateDetailReport.Output output)
            throws UserNotFoundException {

        SecurityContext securityContext = (SecurityContext) UseCaseContext.get();

        Auditor.audit(this.objectMapper, GenerateDetailReport.class, input, null,
                new UserId(Long.valueOf(securityContext.getUserId())));
    }

}
