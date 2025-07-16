package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.query.FindAccountNumberByDfspCodeQuery;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateStatementReportCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GenerateStatementReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class GenerateStatementReportHandler
    extends OperationPortalAuditableUseCase<GenerateStatementReport.Input, GenerateStatementReport.Output>
    implements GenerateStatementReport {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateStatementReportHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.OPERATION);

    private final GenerateStatementReportCommand generateStatementReportCommand;

    private final FindAccountNumberByDfspCodeQuery findAccountNumberByDfspCodeQuery;

    public GenerateStatementReportHandler(CreateInputAuditCommand createInputAuditCommand,
                                          CreateOutputAuditCommand createOutputAuditCommand,
                                          CreateExceptionAuditCommand createExceptionAuditCommand,
                                          ObjectMapper objectMapper,
                                          PrincipalCache principalCache,
                                          GenerateStatementReportCommand generateStatementReportCommand,
                                          FindAccountNumberByDfspCodeQuery findAccountNumberByDfspCodeQuery) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache);

        this.generateStatementReportCommand = generateStatementReportCommand;
        this.findAccountNumberByDfspCodeQuery = findAccountNumberByDfspCodeQuery;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        FindAccountNumberByDfspCodeQuery.Output accountNumberOutput =
            this.findAccountNumberByDfspCodeQuery.execute(new FindAccountNumberByDfspCodeQuery.Input(input.fspId(),
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
