package com.thitsaworks.operation_portal.usecase.central_ledger.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCaseContext;
import com.thitsaworks.operation_portal.component.security.SecurityContext;
import com.thitsaworks.operation_portal.core.audit.exception.UserNotFoundException;
import com.thitsaworks.operation_portal.core.audit.model.Auditor;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.data.PrincipalData;
import com.thitsaworks.operation_portal.core.participant.query.FindAccountNumberByDfspCode;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateStatementReportCommand;
import com.thitsaworks.operation_portal.usecase.central_ledger.GenerateStatementReport;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GenerateStatementReportHandler extends GenerateStatementReport {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateStatementReportHandler.class);

    private final GenerateStatementReportCommand generateStatementReportCommand;

    private final FindAccountNumberByDfspCode findAccountNumberByDfspCode;

    private final ObjectMapper objectMapper;

    private final PrincipalCache principalCache;

    @Override
    public Output onExecute(Input input) throws Exception {

        FindAccountNumberByDfspCode.Output accountNumberOutput =
                this.findAccountNumberByDfspCode.execute(new FindAccountNumberByDfspCode.Input(input.fspId(),
                                                                                               input.currencyId()));

        GenerateStatementReportCommand.Output output =
                this.generateStatementReportCommand.execute(new GenerateStatementReportCommand.Input(input.startDate(),
                                                                                                     input.endDate(),
                                                                                                     input.fspId(),
                                                                                                     accountNumberOutput.getAccountNumber(),
                                                                                                     input.fileType(),
                                                                                                     input.timezoneOffSet(),
                                                                                                     input.currencyId()));

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

        switch (principalData.userRoleType()) {

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
