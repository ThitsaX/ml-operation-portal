package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantData;
import com.thitsaworks.operation_portal.core.participant.query.ParticipantQuery;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateSettlementDetailReportCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GenerateDetailReport;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GenerateDetailReportHandler
    extends OperationPortalAuditableUseCase<GenerateDetailReport.Input, GenerateDetailReport.Output>
    implements GenerateDetailReport {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateDetailReportHandler.class);

    private final GenerateSettlementDetailReportCommand generateSettlementDetailReportCommand;

    private final ParticipantQuery participantQuery;

    public GenerateDetailReportHandler(CreateInputAuditCommand createInputAuditCommand,
                                       CreateOutputAuditCommand createOutputAuditCommand,
                                       CreateExceptionAuditCommand createExceptionAuditCommand,
                                       ObjectMapper objectMapper,
                                       PrincipalCache principalCache,
                                       ActionAuthorizationManager actionAuthorizationManager,
                                       GenerateSettlementDetailReportCommand generateSettlementDetailReportCommand,
                                       ParticipantQuery participantQuery) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.generateSettlementDetailReportCommand = generateSettlementDetailReportCommand;

        this.participantQuery = participantQuery;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        String dfspName = input.fspId().equalsIgnoreCase("all") ? input.fspId() : "";

        if (!input.fspId().equalsIgnoreCase("all")) {

            Optional<ParticipantData> optionalParticipantData = this.participantQuery.get(input.fspId());

            dfspName = optionalParticipantData.isEmpty() ? input.fspId() : optionalParticipantData.get().description();
        }

        GenerateSettlementDetailReportCommand.Output output = this.generateSettlementDetailReportCommand.execute(
            new GenerateSettlementDetailReportCommand.Input(input.settlementId(),
                                                            input.fspId(),
                                                            dfspName,
                                                            input.fileType(),
                                                            input.timezoneOffset()));

        return new Output(output.settlementDetailRptByte());
    }

}
