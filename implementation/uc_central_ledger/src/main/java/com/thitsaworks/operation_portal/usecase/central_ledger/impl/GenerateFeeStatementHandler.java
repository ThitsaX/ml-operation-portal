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
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateFeeSettlementReportCommand;
import com.thitsaworks.operation_portal.usecase.central_ledger.GenerateFeeSettlementReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GenerateFeeStatementHandler extends GenerateFeeSettlementReport {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateFeeStatementHandler.class);

    private final GenerateFeeSettlementReportCommand generateFeeSettlementReportCommand;

    private final ObjectMapper objectMapper;

    private final PrincipalCache principalCache;

    @Autowired
    public GenerateFeeStatementHandler(GenerateFeeSettlementReportCommand generateFeeSettlementReportCommand,
                                       ObjectMapper objectMapper,
                                       PrincipalCache principalCache) {

        this.generateFeeSettlementReportCommand = generateFeeSettlementReportCommand;
        this.objectMapper = objectMapper;
        this.principalCache = principalCache;
    }

    @Override
    public Output onExecute(Input input) throws Exception {

        GenerateFeeSettlementReportCommand.Output output =
                this.generateFeeSettlementReportCommand.execute(new GenerateFeeSettlementReportCommand.Input(input.getStartDate(),
                                                                                                             input.getEndDate(),
                                                                                                             input.getFromFsp(),
                                                                                                             input.getToFsp(),
                                                                                                             input.getCurrency(),
                                                                                                             input.getTimezone(),
                                                                                                             input.getFileType()));

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

        return switch (principalData.getUserRoleType()) {
            case OPERATION -> true;
            case SUPERUSER, ADMIN, REPORTING -> false;
        };

    }

    @Override

    public void onAudit(GenerateFeeSettlementReport.Input input, GenerateFeeSettlementReport.Output output)
            throws UserNotFoundException {

        SecurityContext securityContext = (SecurityContext) UseCaseContext.get();

        Auditor.audit(this.objectMapper, GenerateFeeSettlementReport.class, input, null,
                      new UserId(Long.valueOf(securityContext.getUserId())));
    }

}
