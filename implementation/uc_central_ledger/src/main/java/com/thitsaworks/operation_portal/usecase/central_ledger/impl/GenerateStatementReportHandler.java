package com.thitsaworks.operation_portal.usecase.central_ledger.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.component.common.identifier.AccessKey;
import com.thitsaworks.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCaseContext;
import com.thitsaworks.operation_portal.component.security.SecurityContext;
import com.thitsaworks.operation_portal.core.audit.exception.UserNotFoundException;
import com.thitsaworks.operation_portal.core.audit.model.Auditor;
import com.thitsaworks.operation_portal.core.iam.query.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.query.data.PrincipalData;
import com.thitsaworks.operation_portal.core.participant.query.FindAccountNumberByDfspCode;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateStatementReportCommand;
import com.thitsaworks.operation_portal.usecase.central_ledger.GenerateStatementReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GenerateStatementReportHandler extends GenerateStatementReport {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateStatementReportHandler.class);

    private final GenerateStatementReportCommand generateStatementReportCommand;

    private final FindAccountNumberByDfspCode findAccountNumberByDfspCode;

    private final ObjectMapper objectMapper;

    private final PrincipalCache principalCache;

    @Autowired
    public GenerateStatementReportHandler(GenerateStatementReportCommand generateStatementReportCommand,
                                          FindAccountNumberByDfspCode findAccountNumberByDfspCode,
                                          ObjectMapper objectMapper,
                                          PrincipalCache principalCache) {

        this.generateStatementReportCommand = generateStatementReportCommand;
        this.findAccountNumberByDfspCode = findAccountNumberByDfspCode;
        this.objectMapper = objectMapper;
        this.principalCache = principalCache;
    }

    @Override
    public Output onExecute(Input input) throws Exception {

        FindAccountNumberByDfspCode.Output accountNumberOutput =
                this.findAccountNumberByDfspCode.execute(new FindAccountNumberByDfspCode.Input(input.getFspId(),
                                                                                               input.getCurrencyId()));

        GenerateStatementReportCommand.Output output =
                this.generateStatementReportCommand.execute(new GenerateStatementReportCommand.Input(input.getStartDate(),
                                                                                                     input.getEndDate(),
                                                                                                     input.getFspId(),
                                                                                                     accountNumberOutput.getAccountNumber(),
                                                                                                     input.getFileType(),
                                                                                                     input.getTimezoneOffSet(),
                                                                                                     input.getCurrencyId()));

        return new Output(output.getStatementRptData());
    }

    @Override
    protected String getName() {

        return GenerateStatementReportCommand.class.getCanonicalName();
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

        return GenerateStatementReportCommand.class.getName();
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
