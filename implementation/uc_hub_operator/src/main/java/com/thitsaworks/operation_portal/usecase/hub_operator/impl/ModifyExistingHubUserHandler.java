package com.thitsaworks.operation_portal.usecase.hub_operator.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.hubuser.command.ModifyHubUser;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.usecase.HubOperatorAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.hub_operator.ModifyExistingHubUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ModifyExistingHubUserHandler
    extends HubOperatorAuditableUseCase<ModifyExistingHubUser.Input, ModifyExistingHubUser.Output>
    implements ModifyExistingHubUser {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyExistingHubUserHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.ADMIN,
                                                                    UserRoleType.OPERATION,
                                                                    UserRoleType.REPORTING,
                                                                    UserRoleType.SUPERUSER);

    private final ModifyHubUser modifyHubUser;

    @Autowired
    public ModifyExistingHubUserHandler(CreateInputAuditCommand createInputAuditCommand,
                                        CreateOutputAuditCommand createOutputAuditCommand,
                                        CreateExceptionAuditCommand createExceptionAuditCommand,
                                        ModifyHubUser modifyHubUser,
                                        ObjectMapper objectMapper,
                                        PrincipalCache principalCache) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache);

        this.modifyHubUser = modifyHubUser;
    }

    @Override
    public Output onExecute(Input input) throws DomainException {

        ModifyHubUser.Output output = this.modifyHubUser.execute(new ModifyHubUser.Input(input.hubUserId(),
                                                                                         input.name(),
                                                                                         input.firstName(),
                                                                                         input.lastName(),
                                                                                         input.jobTitle()));

        return new Output(output.modified(), output.hubUserId());
    }

}
