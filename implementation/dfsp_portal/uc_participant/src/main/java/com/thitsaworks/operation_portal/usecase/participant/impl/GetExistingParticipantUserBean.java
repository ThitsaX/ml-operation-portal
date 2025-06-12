package com.thitsaworks.operation_portal.usecase.participant.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.audit.domain.Auditor;
import com.thitsaworks.operation_portal.audit.exception.UserNotFoundException;
import com.thitsaworks.operation_portal.audit.identity.UserId;
import com.thitsaworks.operation_portal.component.security.SecurityContext;
import com.thitsaworks.operation_portal.component.usecase.UseCaseContext;
import com.thitsaworks.operation_portal.component.misc.persistence.transactional.DfspWriteTransactional;
import com.thitsaworks.operation_portal.iam.identity.AccessKey;
import com.thitsaworks.operation_portal.iam.query.cache.PrincipalCache;
import com.thitsaworks.operation_portal.iam.query.data.PrincipalData;
import com.thitsaworks.operation_portal.participant.query.GetUser;
import com.thitsaworks.operation_portal.usecase.participant.GetExistingParticipantUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class GetExistingParticipantUserBean extends GetExistingParticipantUser {

    private static final Logger LOG = LoggerFactory.getLogger(GetExistingParticipantUserBean.class);

    @Autowired
    private GetUser getUser;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    @Qualifier(PrincipalCache.Strategies.DEFAULT)
    private PrincipalCache principalCache;

    @Override
    @DfspWriteTransactional
    public Output onExecute(Input input) throws Exception {

        GetUser.Output output = this.getUser.execute(new GetUser.Input(input.getParticipantUserId()));

        GetExistingParticipantUser.Output result =
                new GetExistingParticipantUser.Output(output.getParticipantUserId(), output.getName(),
                        output.getEmail(), output.getFirstName(), output.getLastName(), output.getJobTitle(),
                        output.getParticipantId(), output.getCreatedDate().getEpochSecond(), output.getDfspCode());

        return result;
    }

    @Override
    protected String getName() {

        return GetExistingParticipantUser.class.getCanonicalName();
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

        return GetExistingParticipantUser.class.getName();
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

        Auditor.audit(this.objectMapper, GetExistingParticipantUser.class, input, output,
                new UserId(Long.valueOf(securityContext.getUserId())));
    }

}
