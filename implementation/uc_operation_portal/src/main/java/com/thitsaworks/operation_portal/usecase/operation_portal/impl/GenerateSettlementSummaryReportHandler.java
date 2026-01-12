package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantData;
import com.thitsaworks.operation_portal.core.participant.query.ParticipantQuery;
import com.thitsaworks.operation_portal.core.participant.query.UserQuery;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateSettlementReportCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GenerateSettlementSummaryReport;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GenerateSettlementSummaryReportHandler
        extends OperationPortalAuditableUseCase<GenerateSettlementSummaryReport.Input, GenerateSettlementSummaryReport.Output>
        implements GenerateSettlementSummaryReport {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateSettlementSummaryReportHandler.class);

    private final GenerateSettlementReportCommand generateSettlementReportCommand;

    private final ParticipantQuery participantQuery;

    private final UserQuery userQuery;

    public GenerateSettlementSummaryReportHandler(CreateInputAuditCommand createInputAuditCommand,
                                                  CreateOutputAuditCommand createOutputAuditCommand,
                                                  CreateExceptionAuditCommand createExceptionAuditCommand,
                                                  ObjectMapper objectMapper,
                                                  PrincipalCache principalCache,
                                                  ActionAuthorizationManager actionAuthorizationManager,
                                                  GenerateSettlementReportCommand generateSettlementReportCommand,
                                                  ParticipantQuery participantQuery,
                                                  UserQuery userQuery) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.generateSettlementReportCommand = generateSettlementReportCommand;
        this.participantQuery = participantQuery;
        this.userQuery = userQuery;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        String dfspName = input.fspId().equalsIgnoreCase("all") ? input.fspId().toUpperCase() : "";

        if (!input.fspId().equalsIgnoreCase("all")) {

            Optional<ParticipantData> optionalParticipantData = this.participantQuery.get(input.fspId());

            dfspName = (optionalParticipantData.isEmpty() || optionalParticipantData.get().description() == null || optionalParticipantData.get().description().isEmpty()) ?
                    input.fspId().toUpperCase() : optionalParticipantData.get().description();
        }

        GenerateSettlementReportCommand.Output output = this.generateSettlementReportCommand.execute(
                new GenerateSettlementReportCommand.Input(input.fspId(),
                                                          dfspName,
                                                          input.settlementId(),
                                                          input.fileType(),
                                                          input.timezoneOffset(),
                                                          this.userQuery.get(new UserId(input.userId())).name()));

        return new Output(output.settlementRptByte());
    }

}
