package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.query.ParticipantQuery;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateSettlementBankReportCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GenerateSettlementBankReport;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GenerateSettlementBankReportHandler
        extends OperationPortalAuditableUseCase<GenerateSettlementBankReport.Input, GenerateSettlementBankReport.Output>
        implements GenerateSettlementBankReport {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateSettlementBankReportHandler.class);

    private final GenerateSettlementBankReportCommand generateSettlementBankReportCommand;

    private final ParticipantQuery participantQuery;

    public GenerateSettlementBankReportHandler(CreateInputAuditCommand createInputAuditCommand,
                                               CreateOutputAuditCommand createOutputAuditCommand,
                                               CreateExceptionAuditCommand createExceptionAuditCommand,
                                               ObjectMapper objectMapper,
                                               PrincipalCache principalCache,
                                               ParticipantQuery participantQuery,
                                               ActionAuthorizationManager actionAuthorizationManager,
                                               GenerateSettlementBankReportCommand generateSettlementBankReportCommand) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.generateSettlementBankReportCommand = generateSettlementBankReportCommand;
        this.participantQuery = participantQuery;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        GenerateSettlementBankReportCommand.Output output =
                this.generateSettlementBankReportCommand.execute(new GenerateSettlementBankReportCommand.Input(input.settlementId(),
                                                                                                               input.currencyId(),
                                                                                                               input.fileType(),
                                                                                                               input.timezone(),
                                                                                                               input.user()));

        return new Output(output.settlementBankRptByte());
    }

}
