package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.cache.ParticipantCache;
import com.thitsaworks.operation_portal.core.participant.cache.UserCache;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantData;
import com.thitsaworks.operation_portal.core.participant.data.UserData;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantErrors;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateGuineaSettlementBankReportCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GenerateGuineaSettlementBankReport;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GenerateGuineaSettlementBankReportHandler
        extends OperationPortalAuditableUseCase<GenerateGuineaSettlementBankReport.Input, GenerateGuineaSettlementBankReport.Output>
        implements GenerateGuineaSettlementBankReport {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateGuineaSettlementBankReportHandler.class);

    private final GenerateGuineaSettlementBankReportCommand generateGuineaSettlementBankReportCommand;

    private final ParticipantCache participantCache;

    private final UserCache userCache;

    public GenerateGuineaSettlementBankReportHandler(CreateInputAuditCommand createInputAuditCommand,
                                                     CreateOutputAuditCommand createOutputAuditCommand,
                                                     CreateExceptionAuditCommand createExceptionAuditCommand,
                                                     ObjectMapper objectMapper,
                                                     PrincipalCache principalCache,
                                                     ActionAuthorizationManager actionAuthorizationManager,
                                                     GenerateGuineaSettlementBankReportCommand generateGuineaSettlementBankReportCommand,
                                                     ParticipantCache participantCache,
                                                     UserCache userCache
                                                    ) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.generateGuineaSettlementBankReportCommand = generateGuineaSettlementBankReportCommand;
        this.participantCache = participantCache;
        this.userCache = userCache;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {
 
        final UserData userData = userCache.get(new UserId(input.userId()));

        if (userData == null) {

            throw new ParticipantException(ParticipantErrors.USER_NOT_FOUND.format(input.userId()
                                                                                        .toString()));
        }

        final ParticipantData userParticipant = participantCache.get(userData.participantId());

        if (userParticipant == null) {

            throw new ParticipantException(ParticipantErrors.PARTICIPANT_NOT_FOUND.format(userData.participantId()
                                                                                                  .getId()
                                                                                                  .toString()));
        }

        GenerateGuineaSettlementBankReportCommand.Output output =
                this.generateGuineaSettlementBankReportCommand.execute(new GenerateGuineaSettlementBankReportCommand.Input(
                        input.settlementId(),
                        input.currencyId().toUpperCase(),
                        input.fileType(),
                        input.timezone(),
                        userData.name(),
                        userParticipant.participantName()
                                       .getValue()));

        return new Output(output.settlementBankRptByte());
    }

}
