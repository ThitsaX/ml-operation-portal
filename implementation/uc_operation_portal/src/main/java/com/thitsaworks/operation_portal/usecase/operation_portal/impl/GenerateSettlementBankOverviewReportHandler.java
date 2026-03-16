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
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateSettlementBankOverviewReportCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GenerateSettlementBankOverviewReport;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GenerateSettlementBankOverviewReportHandler
        extends OperationPortalAuditableUseCase<GenerateSettlementBankOverviewReport.Input, GenerateSettlementBankOverviewReport.Output>
        implements GenerateSettlementBankOverviewReport {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateSettlementBankOverviewReportHandler.class);

    private final GenerateSettlementBankOverviewReportCommand generateSettlementBankOverviewReportCommand;

    private final ParticipantCache participantCache;

    private final UserCache userCache;

    public GenerateSettlementBankOverviewReportHandler(CreateInputAuditCommand createInputAuditCommand,
                                                       CreateOutputAuditCommand createOutputAuditCommand,
                                                       CreateExceptionAuditCommand createExceptionAuditCommand,
                                                       ObjectMapper objectMapper,
                                                       PrincipalCache principalCache,
                                                       ActionAuthorizationManager actionAuthorizationManager,
                                                       GenerateSettlementBankOverviewReportCommand generateSettlementBankOverviewReportCommand,
                                                       ParticipantCache participantCache,
                                                       UserCache userCache
                                                      ) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.generateSettlementBankOverviewReportCommand = generateSettlementBankOverviewReportCommand;
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

        boolean isParent = userParticipant.parentParticipantName() == null
                           || userParticipant.parentParticipantName().isBlank();

        GenerateSettlementBankOverviewReportCommand.Output output =
                this.generateSettlementBankOverviewReportCommand.execute(new GenerateSettlementBankOverviewReportCommand.Input(
                        input.settlementId(),
                        input.currencyId().toUpperCase(),
                        input.fileType(),
                        input.timezone(),
                        userData.name(),
                        userParticipant.participantName()
                                       .getValue(),
                        isParent));

        return new Output(output.settlementBankRptByte());
    }

}
