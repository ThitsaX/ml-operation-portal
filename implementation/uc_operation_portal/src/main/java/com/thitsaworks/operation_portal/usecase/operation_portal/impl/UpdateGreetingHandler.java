package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.hubuser.command.UpdateGreetingCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.UpdateGreeting;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UpdateGreetingHandler extends OperationPortalAuditableUseCase<UpdateGreeting.Input,UpdateGreeting.Output>
    implements UpdateGreeting {

    private final UpdateGreetingCommand updateGreetingCommand;

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.ADMIN,
                                                                    UserRoleType.OPERATION,
                                                                    UserRoleType.REPORTING,
                                                                    UserRoleType.SUPERUSER);

    public UpdateGreetingHandler(CreateInputAuditCommand createInputAuditCommand,
                                 CreateOutputAuditCommand createOutputAuditCommand,
                                 CreateExceptionAuditCommand createExceptionAuditCommand,
                                 ObjectMapper objectMapper,
                                 PrincipalCache principalCache, UpdateGreetingCommand updateGreetingCommand) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache);
        this.updateGreetingCommand = updateGreetingCommand;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        var output = this.updateGreetingCommand.execute(new UpdateGreetingCommand.Input(input.greetingId(),
                                                                                        input.greetingTitle(),
                                                                                        input.greetingDetail(),
                                                                                        input.isDeleted(),
                                                                                        input.greetingDate()));

        return  new Output(output.greetingId());
    }

}
