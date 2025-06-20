package com.thitsaworks.operation_portal.usecase.hub_operator.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.type.RealmType;
import com.thitsaworks.operation_portal.component.misc.security.SecurityContext;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCaseContext;
import com.thitsaworks.operation_portal.core.audit.exception.UserNotFoundException;
import com.thitsaworks.operation_portal.core.audit.model.Auditor;
import com.thitsaworks.operation_portal.core.hubuser.command.CreateHubUser;
import com.thitsaworks.operation_portal.core.iam.command.CreatePrincipal;
import com.thitsaworks.operation_portal.usecase.hub_operator.CreateNewHubUser;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateNewHubUserBean extends CreateNewHubUser {

    private static final Logger LOG = LoggerFactory.getLogger(CreateNewHubUserBean.class);

    private final CreateHubUser createHubUser;

    private final CreatePrincipal createPrincipal;

    private final ObjectMapper objectMapper;
    
    @Override
    public Output onExecute(Input input) throws Exception {

        CreateHubUser.Output output =
                this.createHubUser.execute(
                        new CreateHubUser.Input(input.name(), input.email(),
                                                input.firstName(), input.lastName(), input.jobTitle()));

        CreatePrincipal.Output createAccessOutput = this.createPrincipal.execute(
                new CreatePrincipal.Input(new PrincipalId(output.hubUserId().getId()), RealmType.HUB_OPERATOR,
                                          input.password(), null, input.userRoleType(), input.activeStatus()));

        return new CreateNewHubUser.Output(output.hubUserId(), createAccessOutput.accessKey(),
                                           createAccessOutput.secretKey());
    }

    @Override
    protected String getName() {
        return CreateNewHubUser.class.getCanonicalName();
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
        return CreateNewHubUser.class.getName();
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
    public void onAudit(CreateNewHubUser.Input input, CreateNewHubUser.Output output) throws UserNotFoundException {
        SecurityContext securityContext = (SecurityContext) UseCaseContext.get();

        Auditor.audit(this.objectMapper, CreateNewHubUser.class, input, output,
                      new UserId(securityContext.userId()),
                      securityContext.realmId() == null ? null : new RealmId(securityContext.realmId()));
    }
}