package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.query.IAMQuery;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateAuditReportCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GenerateAuditReport;
import com.thitsaworks.operation_portal.usecase.util.UserPermissionManager;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.ConnectException;
import java.util.List;

@Service
public class GenerateAuditReportHandler
    extends OperationPortalAuditableUseCase<GenerateAuditReport.Input, GenerateAuditReport.Output>
    implements GenerateAuditReport {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateAuditReportHandler.class);

    private final IAMQuery iamQuery;

    private final UserPermissionManager userPermissionManager;

    private final GenerateAuditReportCommand generateAuditReportCommand;

    public GenerateAuditReportHandler(CreateInputAuditCommand createInputAuditCommand,
                                      CreateOutputAuditCommand createOutputAuditCommand,
                                      CreateExceptionAuditCommand createExceptionAuditCommand,
                                      ObjectMapper objectMapper,
                                      PrincipalCache principalCache,
                                      ActionAuthorizationManager actionAuthorizationManager,
                                      IAMQuery iamQuery,
                                      UserPermissionManager userPermissionManager,
                                      GenerateAuditReportCommand generateAuditReportCommand) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.iamQuery = iamQuery;
        this.generateAuditReportCommand = generateAuditReportCommand;
        this.userPermissionManager = userPermissionManager;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException, ConnectException {

        String realmId = null;
        if (this.userPermissionManager.isDfsp(new PrincipalId(input.auditedById()
                                                                   .getEntityId()))) {
            realmId = input.realmId()
                           .getEntityId()
                           .toString();
        }

        var
            userId = input.userId() == null ? null : input.userId()
                                                          .getEntityId()
                                                          .toString();

        var actionId = input.actionId() == null ? null : input.actionId()
                                                              .getEntityId()
                                                              .toString();

        List<String> grantedActionList = this.iamQuery.getGrantedActionsByPrincipal(new PrincipalId(input.auditedById()
                                                                                                         .getEntityId()))
                                                      .stream()
                                                      .map(action -> String.valueOf(action.actionId()
                                                                                          .getEntityId()))
                                                      .toList();

        var output = this.generateAuditReportCommand.execute(new GenerateAuditReportCommand.Input(realmId,
                                                                                                  input.fromDate(),
                                                                                                  input.toDate(),
                                                                                                  input.timezoneoffset(),
                                                                                                  userId,
                                                                                                  actionId,
                                                                                                  input.fileType(),
                                                                                                  grantedActionList));

        return new Output(output.rptData());
    }

}
