package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.query.ParticipantQuery;
import com.thitsaworks.operation_portal.core.participant.query.UserQuery;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateFeeSettlementReportCommand;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateManagementSummaryReportCommand;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateSettlementDetailReportCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GenerateManagementSummaryReport;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GenerateManagementSummaryReportHandler
    extends OperationPortalAuditableUseCase<GenerateManagementSummaryReport.Input, GenerateManagementSummaryReport.Output>
    implements GenerateManagementSummaryReport {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateManagementSummaryReportHandler.class);

    private final GenerateManagementSummaryReportCommand generateManagementSummaryReportCommand;

    private final ParticipantQuery participantQuery;

    private final UserQuery userQuery;

    public GenerateManagementSummaryReportHandler(CreateInputAuditCommand createInputAuditCommand,
                                                  CreateOutputAuditCommand createOutputAuditCommand,
                                                  CreateExceptionAuditCommand createExceptionAuditCommand,
                                                  ObjectMapper objectMapper,
                                                  PrincipalCache principalCache,
                                                  ActionAuthorizationManager actionAuthorizationManager,
                                                  GenerateManagementSummaryReportCommand generateManagementSummaryReportCommand,
                                                  ParticipantQuery participantQuery,
                                                  UserQuery userQuery){
        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.generateManagementSummaryReportCommand = generateManagementSummaryReportCommand;
        this.userQuery = userQuery;
        this.participantQuery = participantQuery;
    }
    @Override
    protected Output onExecute(Input input) throws DomainException {

        var getUser = this.userQuery.get(new UserId(input.userId()));

        GenerateManagementSummaryReportCommand.Output output = this.generateManagementSummaryReportCommand.execute(new GenerateManagementSummaryReportCommand.Input(input.startDate(),
                                                                                                      input.endDate(),
                                                                                                      input.timezoneOffset(),
                                                                                                      input.fileType(),getUser.name()));
        return new Output(output.managementSummaryRptByte());
    }
}
