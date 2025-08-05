package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateSettlementReportCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GenerateSettlementReport;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GenerateSettlementReportHandler
    extends OperationPortalAuditableUseCase<GenerateSettlementReport.Input, GenerateSettlementReport.Output>
    implements GenerateSettlementReport {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateSettlementReportHandler.class);

    private final GenerateSettlementReportCommand generateSettlementReportCommand;

    public GenerateSettlementReportHandler(CreateInputAuditCommand createInputAuditCommand,
                                           CreateOutputAuditCommand createOutputAuditCommand,
                                           CreateExceptionAuditCommand createExceptionAuditCommand,
                                           ObjectMapper objectMapper,
                                           PrincipalCache principalCache,
                                           ActionAuthorizationManager actionAuthorizationManager,
                                           GenerateSettlementReportCommand generateSettlementReportCommand) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.generateSettlementReportCommand = generateSettlementReportCommand;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        GenerateSettlementReportCommand.Output output = this.generateSettlementReportCommand.execute(
            new GenerateSettlementReportCommand.Input(input.fspId(),
                                                      input.settlementId(),
                                                      input.fileType(),
                                                      input.timezoneOffset()));

        return new Output(output.settlementByte());
    }

}
