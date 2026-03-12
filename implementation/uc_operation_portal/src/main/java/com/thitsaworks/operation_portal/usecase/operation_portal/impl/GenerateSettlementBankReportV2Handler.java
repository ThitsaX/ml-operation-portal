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
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateSettlementBankReportV2Command;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GenerateSettlementBankReportV2;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GenerateSettlementBankReportV2Handler
        extends OperationPortalAuditableUseCase<GenerateSettlementBankReportV2.Input, GenerateSettlementBankReportV2.Output>
        implements GenerateSettlementBankReportV2 {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateSettlementBankReportV2Handler.class);

    private final GenerateSettlementBankReportV2Command generateSettlementBankReportV2Command;

    private final ParticipantQuery participantQuery;

    private final UserQuery userQuery;

    public GenerateSettlementBankReportV2Handler(CreateInputAuditCommand createInputAuditCommand,
                                               CreateOutputAuditCommand createOutputAuditCommand,
                                               CreateExceptionAuditCommand createExceptionAuditCommand,
                                               ObjectMapper objectMapper,
                                               PrincipalCache principalCache,
                                               ParticipantQuery participantQuery,
                                               ActionAuthorizationManager actionAuthorizationManager,
                                               GenerateSettlementBankReportV2Command generateSettlementBankReportV2Command,
                                               UserQuery userQuery
                                               ) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.generateSettlementBankReportV2Command = generateSettlementBankReportV2Command;
        this.participantQuery = participantQuery;
        this.userQuery = userQuery;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        var getUser = this.userQuery.get(new UserId(input.userId()));

        GenerateSettlementBankReportV2Command.Output output =
                this.generateSettlementBankReportV2Command.execute(new GenerateSettlementBankReportV2Command.Input(input.settlementId(),
                                                                                                               input.currencyId().toUpperCase(),
                                                                                                               input.fileType(),
                                                                                                               input.timezone(),
                                                                                                               getUser.name()));

        return new Output(output.settlementBankRptByte());
    }

}
