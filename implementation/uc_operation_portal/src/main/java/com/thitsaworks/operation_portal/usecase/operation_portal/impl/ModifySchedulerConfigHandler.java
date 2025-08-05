package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.scheduler.command.CreateOrUpdateSchedulerConfigCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.ModifySchedulerConfig;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ModifySchedulerConfigHandler
    extends OperationPortalAuditableUseCase<ModifySchedulerConfig.Input, ModifySchedulerConfig.Output>
    implements ModifySchedulerConfig {

    private static final Logger LOG = LoggerFactory.getLogger(ModifySchedulerConfigHandler.class);

    private final CreateOrUpdateSchedulerConfigCommand updateSchedulerConfigCommand;

    public ModifySchedulerConfigHandler(CreateInputAuditCommand createInputAuditCommand,
                                        CreateOutputAuditCommand createOutputAuditCommand,
                                        CreateExceptionAuditCommand createExceptionAuditCommand,
                                        ObjectMapper objectMapper,
                                        PrincipalCache principalCache,
                                        CreateOrUpdateSchedulerConfigCommand updateSchedulerConfigCommand,
                                        ActionAuthorizationManager actionAuthorizationManager) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.updateSchedulerConfigCommand = updateSchedulerConfigCommand;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        var
            output =
            this.updateSchedulerConfigCommand.execute(new CreateOrUpdateSchedulerConfigCommand.Input(input.name(),
                                                                                                     input.cronExpression(),
                                                                                                     input.description(),
                                                                                                     input.active()));
        return new Output(output.created());
    }

}
