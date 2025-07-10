package com.thitsaworks.operation_portal.usecase.hub_services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateSettlementReportCommand;
import com.thitsaworks.operation_portal.usecase.HubServicesAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.hub_services.GenerateSettlementReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class GenerateSettlementReportHandler
    extends HubServicesAuditableUseCase<GenerateSettlementReport.Input, GenerateSettlementReport.Output>
    implements GenerateSettlementReport {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateSettlementReportHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.OPERATION);

    private final GenerateSettlementReportCommand generateSettlementReportCommand;

    public GenerateSettlementReportHandler(CreateInputAuditCommand createInputAuditCommand,
                                           CreateOutputAuditCommand createOutputAuditCommand,
                                           CreateExceptionAuditCommand createExceptionAuditCommand,
                                           ObjectMapper objectMapper,
                                           PrincipalCache principalCache,
                                           GenerateSettlementReportCommand generateSettlementReportCommand) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache);

        this.generateSettlementReportCommand = generateSettlementReportCommand;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        GenerateSettlementReportCommand.Output output = this.generateSettlementReportCommand.execute(
            new GenerateSettlementReportCommand.Input(input.fspId(),
                                                      input.settlementId(),
                                                      input.fileType(),
                                                      input.timezoneOffset()));

        return new Output(output.settlementByte());
    }

}
