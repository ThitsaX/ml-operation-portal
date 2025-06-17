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
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateSettlementReportCommand;
import com.thitsaworks.operation_portal.usecase.central_ledger.GenerateSettlementReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GenerateSettlementReportHandler extends GenerateSettlementReport {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateSettlementReportHandler.class);

    private final GenerateSettlementReportCommand generateSettlementReportCommand;

    private final ObjectMapper objectMapper;

    private final PrincipalCache principalCache;

    @Autowired
    public GenerateSettlementReportHandler(GenerateSettlementReportCommand generateSettlementReportCommand,
                                           ObjectMapper objectMapper,
                                           PrincipalCache principalCache) {

        this.generateSettlementReportCommand = generateSettlementReportCommand;
        this.objectMapper = objectMapper;
        this.principalCache = principalCache;
    }

    @Override
    public Output onExecute(Input input) throws Exception {

        GenerateSettlementReportCommand.Output output = this.generateSettlementReportCommand.execute(
                new GenerateSettlementReportCommand.Input(input.getFspId(),
                                                          input.getSettlementId(),
                                                          input.getFileType(),
                                                          input.getTimezoneOffset()));

        return new Output(output.getSettlementByte());
    }

    @Override
    protected String getName() {

        return GenerateSettlementReport.class.getCanonicalName();
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

        return GenerateSettlementReport.class.getName();
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
    public void onAudit(GenerateSettlementReport.Input input, GenerateSettlementReport.Output output)
            throws UserNotFoundException {

        SecurityContext securityContext = (SecurityContext) UseCaseContext.get();

        Auditor.audit(this.objectMapper, GenerateSettlementReport.class, input, null,
                      new UserId(Long.valueOf(securityContext.getUserId())));
    }

}
