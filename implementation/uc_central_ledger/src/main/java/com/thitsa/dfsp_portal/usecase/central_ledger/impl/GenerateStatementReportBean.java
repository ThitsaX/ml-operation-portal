package com.thitsa.dfsp_portal.usecase.central_ledger.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsa.dfsp_portal.report.query.GenerateStatementRpt;
import com.thitsa.dfsp_portal.usecase.central_ledger.GenerateStatementReport;
import com.thitsaworks.dfsp_portal.audit.domain.Auditor;
import com.thitsaworks.dfsp_portal.audit.exception.UserNotFoundException;
import com.thitsaworks.dfsp_portal.audit.identity.UserId;
import com.thitsaworks.dfsp_portal.component.security.SecurityContext;
import com.thitsaworks.dfsp_portal.component.usecase.UseCaseContext;
import com.thitsaworks.dfsp_portal.datasource.persistence.CentralLedgerReadTransactional;
import com.thitsaworks.dfsp_portal.iam.identity.AccessKey;
import com.thitsaworks.dfsp_portal.iam.query.cache.PrincipalCache;
import com.thitsaworks.dfsp_portal.iam.query.data.PrincipalData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class GenerateStatementReportBean extends GenerateStatementReport {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateStatementReportBean.class);

    @Autowired
    private GenerateStatementRpt generateStatementRpt;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    @Qualifier(PrincipalCache.Strategies.DEFAULT)
    private PrincipalCache principalCache;


    @Override
    @CentralLedgerReadTransactional
    public Output onExecute(Input input) throws Exception {

        GenerateStatementRpt.Output output = this.generateStatementRpt.execute(new GenerateStatementRpt.Input(input.getStartDate(),input.getEndDate(),input.getFspId(),input.getFileType(),input.getTimezoneOffSet(),input.getCurrencyId()));

        return new Output(output.getStatementRptData());
    }

    @Override
    protected String getName() {

        return GenerateStatementRpt.class.getCanonicalName();
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

        return GenerateStatementRpt.class.getName();
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
    public void onAudit(GenerateStatementReport.Input input, GenerateStatementReport.Output output)
            throws UserNotFoundException {

        SecurityContext securityContext = (SecurityContext) UseCaseContext.get();

        Auditor.audit(this.objectMapper, GenerateStatementReport.class, input, null,
                new UserId(Long.valueOf(securityContext.getUserId())));
    }

}
