package com.thitsaworks.dfsp_portal.usecase.hub_operator.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.dfsp_portal.audit.domain.Auditor;
import com.thitsaworks.dfsp_portal.audit.exception.UserNotFoundException;
import com.thitsaworks.dfsp_portal.audit.identity.UserId;
import com.thitsaworks.dfsp_portal.component.security.SecurityContext;
import com.thitsaworks.dfsp_portal.component.usecase.UseCaseContext;
import com.thitsaworks.dfsp_portal.datasource.persistence.WriteTransactional;
import com.thitsaworks.dfsp_portal.hubuser.domain.command.ModifyHubUser;
import com.thitsaworks.dfsp_portal.usecase.hub_operator.ModifyExistingHubUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModifyExistingHubUserBean extends ModifyExistingHubUser {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyExistingHubUserBean.class);

    @Autowired
    private ModifyHubUser modifyHubUser;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @WriteTransactional
    public ModifyExistingHubUser.Output onExecute(ModifyExistingHubUser.Input input) throws Exception {

        ModifyHubUser.Output output = this.modifyHubUser.execute(new ModifyHubUser.Input(input.getHubUserId(),
                input.getName(), input.getFirstName(), input.getLastName(), input.getJobTitle()));

        return new Output(output.isModified(), output.getHubUserId());
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
                new UserId(Long.valueOf(securityContext.getUserId())));
    }

}
