package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantData;
import com.thitsaworks.operation_portal.core.participant.query.ParticipantQuery;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateSettlementReportCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GenerateSettlementReport;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GenerateSettlementReportHandler
    extends OperationPortalAuditableUseCase<GenerateSettlementReport.Input, GenerateSettlementReport.Output>
    implements GenerateSettlementReport {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateSettlementReportHandler.class);

    private final GenerateSettlementReportCommand generateSettlementReportCommand;

    private final ParticipantQuery participantQuery;

    public GenerateSettlementReportHandler(CreateInputAuditCommand createInputAuditCommand,
                                           CreateOutputAuditCommand createOutputAuditCommand,
                                           CreateExceptionAuditCommand createExceptionAuditCommand,
                                           ObjectMapper objectMapper,
                                           PrincipalCache principalCache,
                                           ActionAuthorizationManager actionAuthorizationManager,
                                           GenerateSettlementReportCommand generateSettlementReportCommand,
                                           ParticipantQuery participantQuery) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.generateSettlementReportCommand = generateSettlementReportCommand;
        this.participantQuery = participantQuery;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        String dfspName = input.fspId().equalsIgnoreCase("all") ? input.fspId() : "";

        if (!input.fspId().equalsIgnoreCase("all")) {

            Optional<ParticipantData> optionalParticipantData = this.participantQuery.get(input.fspId());

            dfspName = optionalParticipantData.isEmpty() ? input.fspId() : optionalParticipantData.get().description();
        }

        GenerateSettlementReportCommand.Output output = this.generateSettlementReportCommand.execute(
            new GenerateSettlementReportCommand.Input(input.fspId(),
                                                      dfspName,
                                                      input.settlementId(),
                                                      input.fileType(),
                                                      input.timezoneOffset()));

        return new Output(output.settlementRptByte());
    }

}
