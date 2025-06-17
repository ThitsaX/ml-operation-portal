package com.thitsaworks.operation_portal.usecase.hub_operator.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.audit.domain.Auditor;
import com.thitsaworks.operation_portal.audit.exception.UserNotFoundException;
import com.thitsaworks.operation_portal.audit.identity.UserId;
import com.thitsaworks.operation_portal.component.security.SecurityContext;
import com.thitsaworks.operation_portal.component.usecase.UseCaseContext;
import com.thitsaworks.operation_portal.component.misc.persistence.transactional.DfspWriteTransactional;
import com.thitsaworks.operation_portal.hubuser.query.GetHubUsers;
import com.thitsaworks.operation_portal.usecase.hub_operator.GetAllHubUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetAllHubUserBean extends GetAllHubUser {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllHubUserBean.class);

    @Autowired
    private GetHubUsers getHubUsers;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @DfspWriteTransactional
    public GetAllHubUser.Output onExecute(GetAllHubUser.Input input) throws Exception {

        GetHubUsers.Output output = this.getHubUsers.execute(new GetHubUsers.Input());

        List<Output.HubUserInfo> userInfoList = new ArrayList<>();

        for (GetHubUsers.Output.HubUserInfo data : output.getUserInfoList()) {

            userInfoList.add(
                    new Output.HubUserInfo(data.getHubUserId(), data.getName(), data.getEmail(), data.getFirstName(),
                            data.getLastName(), data.getJobTitle(), data.getCreatedDate()));
        }

        return new GetAllHubUser.Output(userInfoList);
    }

    @Override
    protected String getName() {

        return GetAllHubUser.class.getCanonicalName();
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

        return GetAllHubUser.class.getName();
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

        Auditor.audit(this.objectMapper, GetAllHubUser.class, input, output,
                new UserId(Long.valueOf(securityContext.getUserId())));
    }

}
