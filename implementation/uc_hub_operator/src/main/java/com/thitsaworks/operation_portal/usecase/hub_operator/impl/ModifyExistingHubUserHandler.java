package com.thitsaworks.operation_portal.usecase.hub_operator.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCaseContext;
import com.thitsaworks.operation_portal.component.misc.security.SecurityContext;
import com.thitsaworks.operation_portal.core.audit.exception.UserNotFoundException;
import com.thitsaworks.operation_portal.core.audit.model.Auditor;
import com.thitsaworks.operation_portal.core.hubuser.command.ModifyHubUser;
import com.thitsaworks.operation_portal.usecase.hub_operator.ModifyExistingHubUser;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModifyExistingHubUserHandler extends ModifyExistingHubUser {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyExistingHubUserHandler.class);

    private final ModifyHubUser modifyHubUser;

    private final ObjectMapper objectMapper;

    @Override
    public ModifyExistingHubUser.Output onExecute(ModifyExistingHubUser.Input input) throws Exception {

        ModifyHubUser.Output output = this.modifyHubUser.execute(new ModifyHubUser.Input(input.hubUserId(),
                                                                                         input.name(),
                                                                                         input.firstName(),
                                                                                         input.lastName(),
                                                                                         input.jobTitle()));

        return new Output(output.modified(), output.hubUserId());
    }

    @Override
    protected String getName() {

        return ModifyExistingHubUser.class.getCanonicalName();
    }

    @Override
    protected String getDescription() {

        return null;
    }

    @Override
    protected String getScope() {

        return "uc_hub_operator";
    }

    @Override
    protected String getId() {

        return ModifyExistingHubUser.class.getName();
    }

    @Override
    public boolean isOwned(Object userDetails) {

        return true;
    }

    @Override
    public boolean isAuthorized(Object userDetails) {

        return true;
    }

    @Override
    public void onAudit(ModifyExistingHubUser.Input input, ModifyExistingHubUser.Output output)
            throws UserNotFoundException {

        SecurityContext securityContext = (SecurityContext) UseCaseContext.get();

        Auditor.audit(this.objectMapper, ModifyExistingHubUser.class, input, output,
                      new UserId(securityContext.userId()),
                      securityContext.realmId() == null ? null : new RealmId(securityContext.realmId()));
    }

}
