package com.thitsaworks.operation_portal.usecase.participant.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.audit.domain.Auditor;
import com.thitsaworks.operation_portal.audit.exception.UserNotFoundException;
import com.thitsaworks.operation_portal.audit.identity.UserId;
import com.thitsaworks.operation_portal.component.security.SecurityContext;
import com.thitsaworks.operation_portal.component.usecase.UseCaseContext;
import com.thitsaworks.operation_portal.component.misc.persistence.transactional.DfspWriteTransactional;
import com.thitsaworks.operation_portal.iam.identity.AccessKey;
import com.thitsaworks.operation_portal.iam.identity.PrincipalId;
import com.thitsaworks.operation_portal.iam.query.cache.PrincipalCache;
import com.thitsaworks.operation_portal.iam.query.data.PrincipalData;
import com.thitsaworks.operation_portal.participant.query.GetUsers;
import com.thitsaworks.operation_portal.usecase.participant.GetAllParticipantUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetAllParticipantUserBean extends GetAllParticipantUser {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllParticipantUserBean.class);

    @Autowired
    private GetUsers getUsers;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    @Qualifier(PrincipalCache.Strategies.DEFAULT)
    private PrincipalCache principalCache;

    @Override
    @DfspWriteTransactional
    public Output onExecute(Input input) throws Exception {

        GetUsers.Output output = this.getUsers.execute(new GetUsers.Input(input.getParticipantId()));


        List<Output.UserInfo> userInfoList = new ArrayList<>();

        for (GetUsers.Output.UserInfo data : output.getUserInfoList()) {

            PrincipalData principalData =    principalCache.get(new PrincipalId(data.getParticipantUserId().getId()));

            userInfoList.add(new Output.UserInfo(data.getParticipantUserId(), data.getName(), data.getEmail(),
                    data.getFirstName(), data.getLastName(), data.getJobTitle(),principalData.getUserRoleType().toString(), data.getStatus(),
                    data.getCreatedDate()));
        }

        return new GetAllParticipantUser.Output(userInfoList);
    }

    @Override
    protected String getName() {

        return GetAllParticipantUser.class.getCanonicalName();
    }

    @Override
    protected String getDescription() {

        return null;
    }

    @Override
    protected String getScope() {

        return "uc_participant";
    }

    @Override
    protected String getId() {

        return GetAllParticipantUser.class.getName();
    }

    @Override
    public boolean isOwned(Object userDetails) {

        return true;
    }

    @Override
    public boolean isAuthorized(Object userDetails) {

        SecurityContext securityContext = (SecurityContext) userDetails;

        PrincipalData principalData =
                this.principalCache.get(new AccessKey(Long.parseLong(securityContext.getAccessKey())));

        switch (principalData.getUserRoleType()) {

            case ADMIN:
                return true;
            case OPERATION:
            case SUPERUSER:
            case REPORTING:
                return false;
        }

        return false;
    }

    @Override
    public void onAudit(Input input, Output output) throws UserNotFoundException {

        SecurityContext securityContext = (SecurityContext) UseCaseContext.get();

        Auditor.audit(this.objectMapper, GetAllParticipantUser.class, input, output,
                new UserId(Long.valueOf(securityContext.getUserId())));
    }

}
