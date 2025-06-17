package com.thitsaworks.operation_portal.usecase.participant.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.component.common.identifier.AccessKey;
import com.thitsaworks.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.misc.persistence.transactional.DfspWriteTransactional;
import com.thitsaworks.operation_portal.component.misc.spring.CacheQualifiers;
import com.thitsaworks.operation_portal.component.security.SecurityContext;
import com.thitsaworks.operation_portal.core.iam.query.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.query.data.PrincipalData;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantUserData;
import com.thitsaworks.operation_portal.core.participant.query.ParticipantUserQuery;
import com.thitsaworks.operation_portal.usecase.participant.GetAllParticipantUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class GetAllParticipantUserBean extends GetAllParticipantUser {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllParticipantUserBean.class);

    private final ParticipantUserQuery participantUserQuery;

    private final ObjectMapper objectMapper;

    private final PrincipalCache principalCache;

    @Autowired
    public GetAllParticipantUserBean(ParticipantUserQuery participantUserQuery,
                                     ObjectMapper objectMapper,
                                     @Qualifier(CacheQualifiers.DEFAULT) PrincipalCache principalCache) {

        this.participantUserQuery = participantUserQuery;
        this.objectMapper = objectMapper;
        this.principalCache = principalCache;
    }

    @Override
    public Output onExecute(Input input) throws Exception {

        List<ParticipantUserData> participantUserDataList =
                this.participantUserQuery.getParticipantUsers(input.participantId());

        List<UserInfo> userInfoList = new ArrayList<>();

        for (ParticipantUserData participantUserData : participantUserDataList) {

            PrincipalData principalData = principalCache.get(new PrincipalId(participantUserData.participantUserId()
                                                                                                .getId()));

            userInfoList.add(new UserInfo(participantUserData.participantUserId(),
                                          participantUserData.name(),
                                          participantUserData.email(),
                                          participantUserData.firstName(),
                                          participantUserData.lastName(),
                                          participantUserData.jobTitle(),
                                          principalData.getUserRoleType().toString(),
                                          participantUserData.status().toString(),
                                          Instant.ofEpochSecond(participantUserData.createdDate())));
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
    public void onAudit(Input input, Output output) {

//        SecurityContext securityContext = (SecurityContext) UseCaseContext.get();
//
//        Auditor.audit(this.objectMapper, GetAllParticipantUser.class, input, output,
//                      new UserId(Long.valueOf(securityContext.getUserId())));
    }

}
