package com.thitsaworks.operation_portal.usecase.hub_operator.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.audit.domain.Auditor;
import com.thitsaworks.operation_portal.audit.exception.UserNotFoundException;
import com.thitsaworks.operation_portal.audit.identity.UserId;
import com.thitsaworks.operation_portal.component.security.SecurityContext;
import com.thitsaworks.operation_portal.component.usecase.UseCaseContext;
import com.thitsaworks.operation_portal.component.misc.persistence.transactional.DfspWriteTransactional;
import com.thitsaworks.operation_portal.hubuser.domain.command.CreateHubUser;
import com.thitsaworks.operation_portal.iam.domain.command.CreatePrincipal;
import com.thitsaworks.operation_portal.iam.identity.PrincipalId;
import com.thitsaworks.operation_portal.iam.type.RealmType;
import com.thitsaworks.operation_portal.usecase.hub_operator.CreateNewHubUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateNewHubUserBean extends CreateNewHubUser {

    private static final Logger LOG = LoggerFactory.getLogger(CreateNewHubUserBean.class);

    @Autowired
    private CreateHubUser createHubUser;

    @Autowired
    private CreatePrincipal createPrincipal;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @DfspWriteTransactional
    public CreateNewHubUser.Output onExecute(CreateNewHubUser.Input input) throws Exception {

        CreateHubUser.Output output =
                this.createHubUser.execute(
                        new CreateHubUser.Input(input.getName(), input.getEmail(),
                                input.getFirstName(), input.getLastName(), input.getJobTitle()));

        CreatePrincipal.Output createAccessOutput = this.createPrincipal.execute(
                new CreatePrincipal.Input(new PrincipalId(output.getHubUserId().getId()), RealmType.HUB_OPERATOR,
                        input.getPassword(), null, input.getUserRoleType(),input.getActiveStatus()));

        return new CreateNewHubUser.Output(output.getHubUserId(), createAccessOutput.getAccessKey(),
                createAccessOutput.getSecretKey());
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
                new UserId(Long.valueOf(securityContext.getUserId())));
    }

}
