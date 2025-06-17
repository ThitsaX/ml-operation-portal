package com.thitsaworks.operation_portal.usecase.hub_operator.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.audit.domain.Auditor;
import com.thitsaworks.operation_portal.audit.exception.UserNotFoundException;
import com.thitsaworks.operation_portal.audit.identity.UserId;
import com.thitsaworks.operation_portal.component.security.SecurityContext;
import com.thitsaworks.operation_portal.component.usecase.UseCaseContext;
import com.thitsaworks.operation_portal.component.misc.persistence.transactional.DfspWriteTransactional;
import com.thitsaworks.operation_portal.hubuser.query.GetHubUser;
import com.thitsaworks.operation_portal.usecase.hub_operator.GetExistingHubUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetExistingHubUserBean extends GetExistingHubUser {

    private static final Logger LOG = LoggerFactory.getLogger(GetExistingHubUserBean.class);

    @Autowired
    private GetHubUser getHubUser;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @DfspWriteTransactional
    public GetExistingHubUser.Output onExecute(GetExistingHubUser.Input input) throws Exception {

        GetHubUser.Output output = this.getHubUser.execute(new GetHubUser.Input(input.getHubUserId()));

        GetExistingHubUser.Output result =
                new GetExistingHubUser.Output(output.getHubUserId(), output.getName(), output.getEmail(),
                        output.getFirstName(), output.getLastName(), output.getJobTitle(),
                        output.getCreatedDate().getEpochSecond());

        return result;
    }

    @Override
    protected String getName() {

        return GetExistingHubUser.class.getCanonicalName();
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

        return GetExistingHubUser.class.getName();
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
    public void onAudit(Input input, Output output) throws UserNotFoundException {

        SecurityContext securityContext = (SecurityContext) UseCaseContext.get();

        Auditor.audit(this.objectMapper, GetExistingHubUser.class, input, output,
                new UserId(Long.valueOf(securityContext.getUserId())));
    }

}
