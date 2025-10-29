package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateFeeSettlementReportCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GenerateFeeSettlementReport;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GenerateFeeSettlementReportHandler
    extends OperationPortalAuditableUseCase<GenerateFeeSettlementReport.Input, GenerateFeeSettlementReport.Output>
    implements GenerateFeeSettlementReport {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateFeeSettlementReportHandler.class);

    private final GenerateFeeSettlementReportCommand generateFeeSettlementReportCommand;

    public GenerateFeeSettlementReportHandler(CreateInputAuditCommand createInputAuditCommand,
                                              CreateOutputAuditCommand createOutputAuditCommand,
                                              CreateExceptionAuditCommand createExceptionAuditCommand,
                                              ObjectMapper objectMapper,
                                              PrincipalCache principalCache,
                                              ActionAuthorizationManager actionAuthorizationManager,
                                              GenerateFeeSettlementReportCommand generateFeeSettlementReportCommand) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.generateFeeSettlementReportCommand = generateFeeSettlementReportCommand;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        GenerateFeeSettlementReportCommand.Output output =
            this.generateFeeSettlementReportCommand.execute(new GenerateFeeSettlementReportCommand.Input(input.startDate(),
                                                                                                         input.endDate(),
                                                                                                         input.fromFsp(),
                                                                                                         input.toFsp(),
                                                                                                         input.currency(),
                                                                                                         input.timezone(),
                                                                                                         input.fileType()));

        return new Output(output.feeSettlementRptByte());
    }

}
