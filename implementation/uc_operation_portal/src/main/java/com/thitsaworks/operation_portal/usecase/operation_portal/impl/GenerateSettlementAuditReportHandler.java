package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantData;
import com.thitsaworks.operation_portal.core.participant.query.ParticipantQuery;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateSettlementAuditReportCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GenerateSettlementAuditReport;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GenerateSettlementAuditReportHandler
    extends OperationPortalAuditableUseCase<GenerateSettlementAuditReport.Input, GenerateSettlementAuditReport.Output>
    implements GenerateSettlementAuditReport {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateSettlementAuditReportHandler.class);

    private final GenerateSettlementAuditReportCommand generateSettlementAuditReportCommand;

    private final ParticipantQuery participantQuery;

    public GenerateSettlementAuditReportHandler(CreateInputAuditCommand createInputAuditCommand,
                                                CreateOutputAuditCommand createOutputAuditCommand,
                                                CreateExceptionAuditCommand createExceptionAuditCommand,
                                                ObjectMapper objectMapper,
                                                PrincipalCache principalCache,
                                                ActionAuthorizationManager actionAuthorizationManager,
                                                GenerateSettlementAuditReportCommand generateSettlementAuditReportCommand,
                                                ParticipantQuery participantQuery) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.generateSettlementAuditReportCommand = generateSettlementAuditReportCommand;
        this.participantQuery = participantQuery;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        String
            dfspName =
            input.dfspId()
                 .equalsIgnoreCase("all") ? input.dfspId() : "";

        if (!input.dfspId()
                  .equalsIgnoreCase("all")) {

            Optional<ParticipantData> optionalParticipantData = this.participantQuery.get(input.dfspId());

            dfspName =
                optionalParticipantData.isEmpty() ? input.dfspId() : optionalParticipantData.get()
                                                                                            .description();
        }

        GenerateSettlementAuditReportCommand.Output output =
            this.generateSettlementAuditReportCommand.execute(new GenerateSettlementAuditReportCommand.Input(input.startDate(),
                                                                                                             input.endDate(),
                                                                                                             input.dfspId(),
                                                                                                             dfspName,
                                                                                                             input.currencyId(),
                                                                                                             input.fileType(),
                                                                                                             input.timezone()));

        return new Output(output.reportData());
    }

}
