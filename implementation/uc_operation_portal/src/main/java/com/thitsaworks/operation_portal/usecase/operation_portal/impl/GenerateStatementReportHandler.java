package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.query.FindAccountNumberByParticipantNameQuery;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateStatementReportCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GenerateStatementReport;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GenerateStatementReportHandler
    extends OperationPortalAuditableUseCase<GenerateStatementReport.Input, GenerateStatementReport.Output>
    implements GenerateStatementReport {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateStatementReportHandler.class);

    private final GenerateStatementReportCommand generateStatementReportCommand;

    private final FindAccountNumberByParticipantNameQuery findAccountNumberByParticipantNameQuery;

    public GenerateStatementReportHandler(CreateInputAuditCommand createInputAuditCommand,
                                          CreateOutputAuditCommand createOutputAuditCommand,
                                          CreateExceptionAuditCommand createExceptionAuditCommand,
                                          ObjectMapper objectMapper,
                                          PrincipalCache principalCache,
                                          ActionAuthorizationManager actionAuthorizationManager,
                                          GenerateStatementReportCommand generateStatementReportCommand,
                                          FindAccountNumberByParticipantNameQuery findAccountNumberByParticipantNameQuery) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.generateStatementReportCommand = generateStatementReportCommand;
        this.findAccountNumberByParticipantNameQuery = findAccountNumberByParticipantNameQuery;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        FindAccountNumberByParticipantNameQuery.Output accountNumberOutput =
            this.findAccountNumberByParticipantNameQuery.execute(new FindAccountNumberByParticipantNameQuery.Input(input.fspId(),
                                                                                                                   input.currencyId()));

        GenerateStatementReportCommand.Output output =
            this.generateStatementReportCommand.execute(new GenerateStatementReportCommand.Input(input.startDate(),
                                                                                                 input.endDate(),
                                                                                                 input.fspId(),
                                                                                                 accountNumberOutput.accountNumber(),
                                                                                                 input.fileType(),
                                                                                                 input.timezoneOffSet(),
                                                                                                 input.currencyId()));

        return new Output(output.statementRptData());
    }

}
