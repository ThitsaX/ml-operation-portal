package com.thitsaworks.operation_portal.usecase.hub_operator.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.misc.security.SecurityContext;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCaseContext;
import com.thitsaworks.operation_portal.core.audit.exception.UserNotFoundException;
import com.thitsaworks.operation_portal.core.audit.model.Auditor;
import com.thitsaworks.operation_portal.core.hubuser.data.HubUserData;
import com.thitsaworks.operation_portal.core.hubuser.query.HubUserQuery;
import com.thitsaworks.operation_portal.usecase.hub_operator.GetAllHubUser;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllHubUserHandler extends GetAllHubUser {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllHubUserHandler.class);

    private final HubUserQuery hubUserQuery;

    private final ObjectMapper objectMapper;

    @Override
    public Output onExecute(Input input) throws Exception {

        List<HubUserData> hubUserDataList = this.hubUserQuery.getHubUsers();

        List<Output.HubUserInfo> userInfoList = new ArrayList<>();

        for (HubUserData hubUserData : hubUserDataList) {

            userInfoList.add(new Output.HubUserInfo(hubUserData.hubUserId(),
                                                    hubUserData.name(),
                                                    hubUserData.email(),
                                                    hubUserData.firstName(),
                                                    hubUserData.lastName(),
                                                    hubUserData.jobTitle(),
                                                    hubUserData.createdDate()));
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
                      new UserId(securityContext.userId()),
                      securityContext.realmId() == null ? null : new RealmId(securityContext.realmId()));
    }

}
