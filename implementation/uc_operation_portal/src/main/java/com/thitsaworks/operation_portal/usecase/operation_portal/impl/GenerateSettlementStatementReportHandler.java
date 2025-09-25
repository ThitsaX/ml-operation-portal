package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateSettlementStatementReportCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GenerateSettlementStatementReport;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.ConnectException;

@Service
public class GenerateSettlementStatementReportHandler
    extends OperationPortalAuditableUseCase<GenerateSettlementStatementReport.Input, GenerateSettlementStatementReport.Output>
    implements GenerateSettlementStatementReport {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateSettlementReportHandler.class);

    private final GenerateSettlementStatementReportCommand generateSettlementStatementReportCommand;

    public GenerateSettlementStatementReportHandler(CreateInputAuditCommand createInputAuditCommand,
                                                    CreateOutputAuditCommand createOutputAuditCommand,
                                                    CreateExceptionAuditCommand createExceptionAuditCommand,
                                                    ObjectMapper objectMapper,
                                                    PrincipalCache principalCache,
                                                    ActionAuthorizationManager actionAuthorizationManager,
                                                    GenerateSettlementStatementReportCommand generateSettlementStatementReportCommand) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.generateSettlementStatementReportCommand = generateSettlementStatementReportCommand;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException, ConnectException {

        GenerateSettlementStatementReportCommand.Output output = this.generateSettlementStatementReportCommand.execute(
            new GenerateSettlementStatementReportCommand.Input(input.fspId(),
                                                               input.startDate(),
                                                               input.endDate(),
                                                               input.fileType(),
                                                               input.currencyId(),
                                                               input.timezoneOffSet()));
        return new Output(output.statementRptData());
    }

}
