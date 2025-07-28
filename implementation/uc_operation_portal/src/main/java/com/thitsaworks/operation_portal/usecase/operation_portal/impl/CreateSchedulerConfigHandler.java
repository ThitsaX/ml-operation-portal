package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.command.CreateContactCommand;
import com.thitsaworks.operation_portal.core.scheduler.command.CreateOrUpdateSchedulerConfigCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.CreateSchedulerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CreateSchedulerConfigHandler
    extends OperationPortalAuditableUseCase<CreateSchedulerConfig.Input, CreateSchedulerConfig.Output>
    implements CreateSchedulerConfig {

    private static final Logger LOG = LoggerFactory.getLogger(CreateSchedulerConfigHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.OPERATION,
                                                                    UserRoleType.ADMIN);

    private final CreateOrUpdateSchedulerConfigCommand createSchedulerConfigCommand;

    public CreateSchedulerConfigHandler(CreateInputAuditCommand createInputAuditCommand,
                                        CreateOutputAuditCommand createOutputAuditCommand,
                                        CreateExceptionAuditCommand createExceptionAuditCommand,
                                        ObjectMapper objectMapper,
                                        PrincipalCache principalCache,
                                        CreateOrUpdateSchedulerConfigCommand createSchedulerConfigCommand) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache);

        this.createSchedulerConfigCommand = createSchedulerConfigCommand;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        var
            output =
            this.createSchedulerConfigCommand.execute(new CreateOrUpdateSchedulerConfigCommand.Input(input.name(),
                                                                                                     input.cronExpression(),
                                                                                                     input.description(),
                                                                                                     true));
        return new Output(output.created());
    }

}
