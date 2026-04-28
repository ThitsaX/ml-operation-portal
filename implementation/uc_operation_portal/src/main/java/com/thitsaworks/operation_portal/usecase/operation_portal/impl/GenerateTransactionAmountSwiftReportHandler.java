package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateFeeSettlementReportCommand;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateTransactionAmountSwiftReportCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GenerateFeeSettlementReport;
import com.thitsaworks.operation_portal.usecase.operation_portal.GenerateTransactionAmountSwiftReport;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.ConnectException;

@Service
public class GenerateTransactionAmountSwiftReportHandler
    extends OperationPortalAuditableUseCase<GenerateTransactionAmountSwiftReport.Input, GenerateTransactionAmountSwiftReport.Output>
    implements GenerateTransactionAmountSwiftReport {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateTransactionAmountSwiftReportHandler.class);

    private final GenerateTransactionAmountSwiftReportCommand generateTransactionAmountSwiftReportCommand;

    public GenerateTransactionAmountSwiftReportHandler(CreateInputAuditCommand createInputAuditCommand,
                                                       CreateOutputAuditCommand createOutputAuditCommand,
                                                       CreateExceptionAuditCommand createExceptionAuditCommand,
                                                       ObjectMapper objectMapper,
                                                       PrincipalCache principalCache,
                                                       ActionAuthorizationManager actionAuthorizationManager,
                                                       GenerateTransactionAmountSwiftReportCommand generateTransactionAmountSwiftReportCommand
                                                      ) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.generateTransactionAmountSwiftReportCommand = generateTransactionAmountSwiftReportCommand;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException, ConnectException, JsonProcessingException {


        GenerateTransactionAmountSwiftReportCommand.Output output =
            this.generateTransactionAmountSwiftReportCommand.execute(new GenerateTransactionAmountSwiftReportCommand.Input(input.settlementId(),
                                                                                                         input.currencyId(),
                                                                                                         input.timezone()));

        return new GenerateTransactionAmountSwiftReport.Output(output.feeSettlementRptByte());
    }

}
