package com.thitsaworks.operation_portal.usecase.participant.impl;

import com.thitsaworks.operation_portal.component.security.SecurityContext;
import com.thitsaworks.operation_portal.datasource.persistence.WriteTransactional;
import com.thitsaworks.operation_portal.iam.identity.AccessKey;
import com.thitsaworks.operation_portal.iam.identity.PrincipalId;
import com.thitsaworks.operation_portal.iam.query.cache.PrincipalCache;
import com.thitsaworks.operation_portal.iam.query.data.PrincipalData;
import com.thitsaworks.operation_portal.participant.exception.ParticipantNotFoundException;
import com.thitsaworks.operation_portal.participant.exception.ParticipantUserNotFoundException;
import com.thitsaworks.operation_portal.participant.query.GetUser;
import com.thitsaworks.operation_portal.participant.query.cache.ParticipantCache;
import com.thitsaworks.operation_portal.participant.query.cache.ParticipantUserCache;
import com.thitsaworks.operation_portal.participant.query.data.ParticipantData;
import com.thitsaworks.operation_portal.participant.query.data.ParticipantUserData;
import com.thitsaworks.operation_portal.usecase.participant.GetUserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class GetUserProfileBean extends GetUserProfile {

    private static final Logger LOG = LoggerFactory.getLogger(GetUserProfileBean.class);

    @Autowired
    private GetUser getUser;

    @Autowired
    @Qualifier(ParticipantCache.Strategies.DEFAULT)
    private ParticipantCache participantCache;

    @Autowired
    @Qualifier(ParticipantUserCache.Strategies.DEFAULT)
    private ParticipantUserCache participantUserCache;

    @Autowired
    @Qualifier(PrincipalCache.Strategies.DEFAULT)
    private PrincipalCache principalCache;

    @Override
    @WriteTransactional
    public GetUserProfile.Output onExecute(GetUserProfile.Input input) throws Exception {

        ParticipantUserData participantUserData = this.participantUserCache.get(input.getParticipantUserId());

        PrincipalData principalData = this.principalCache.get(new PrincipalId(input.getParticipantUserId().getId()));
        String roleType=principalData.getUserRoleType().toString();

        if (participantUserData == null) {

            throw new ParticipantUserNotFoundException(input.getParticipantUserId().getId().toString());
        }

        ParticipantData participantData = this.participantCache.get(participantUserData.getParticipantId());

        if (participantData == null) {

            throw new ParticipantNotFoundException(participantUserData.getParticipantId().getId().toString());
        }

        GetUser.Output output = this.getUser.execute(new GetUser.Input(input.getParticipantUserId()));

        return new Output(output.getParticipantUserId(), output.getName(), output.getEmail(), output.getFirstName(),
                output.getLastName(), output.getJobTitle(), output.getParticipantId(),
                          output.getCreatedDate().getEpochSecond(),
                          participantData.getDfspCode().getValue(),
                          output.getDfspName(),
                          roleType);

    }

    @Override
    protected String getName() {

        return GetUserProfile.class.getCanonicalName();
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

        return GetUserProfile.class.getName();
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
            case OPERATION:
                return true;
            case REPORTING:
            case SUPERUSER:
                return false;
        }

        return false;
    }



}
