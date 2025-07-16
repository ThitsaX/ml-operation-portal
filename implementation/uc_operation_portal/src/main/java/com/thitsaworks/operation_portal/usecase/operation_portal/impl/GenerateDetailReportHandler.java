package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateSettlementDetailReportCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GenerateDetailReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class GenerateDetailReportHandler
    extends OperationPortalAuditableUseCase<GenerateDetailReport.Input, GenerateDetailReport.Output>
    implements GenerateDetailReport {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateDetailReportHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.OPERATION);

    private final GenerateSettlementDetailReportCommand generateSettlementDetailReportCommand;

    public GenerateDetailReportHandler(CreateInputAuditCommand createInputAuditCommand,
                                       CreateOutputAuditCommand createOutputAuditCommand,
                                       CreateExceptionAuditCommand createExceptionAuditCommand,
                                       ObjectMapper objectMapper,
                                       PrincipalCache principalCache,
                                       GenerateSettlementDetailReportCommand generateSettlementDetailReportCommand) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache);

        this.generateSettlementDetailReportCommand = generateSettlementDetailReportCommand;

    }
    
    @Override
    protected Output onExecute(Input input) throws DomainException {

        GenerateSettlementDetailReportCommand.Output output = this.generateSettlementDetailReportCommand.execute(
            new GenerateSettlementDetailReportCommand.Input(input.settlementId(),
                                                            input.fspId(),
                                                            input.fileType(),
                                                            input.timezoneOffset()));

        return new Output(output.detailReportByte());
    }

}
