package com.thitsaworks.dfsp_portal.usecase.common.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.dfsp_portal.audit.domain.Auditor;
import com.thitsaworks.dfsp_portal.audit.exception.UserNotFoundException;
import com.thitsaworks.dfsp_portal.audit.identity.UserId;
import com.thitsaworks.dfsp_portal.component.security.SecurityContext;
import com.thitsaworks.dfsp_portal.component.usecase.UseCaseContext;
import com.thitsaworks.dfsp_portal.datasource.persistence.WriteTransactional;
import com.thitsaworks.dfsp_portal.iam.domain.command.CreatePrincipal;
import com.thitsaworks.dfsp_portal.iam.exception.PrincipalNotFoundException;
import com.thitsaworks.dfsp_portal.iam.exception.UnauthorizedCreationException;
import com.thitsaworks.dfsp_portal.iam.identity.AccessKey;
import com.thitsaworks.dfsp_portal.iam.identity.PrincipalId;
import com.thitsaworks.dfsp_portal.iam.identity.RealmId;
import com.thitsaworks.dfsp_portal.iam.query.cache.PrincipalCache;
import com.thitsaworks.dfsp_portal.iam.query.data.PrincipalData;
import com.thitsaworks.dfsp_portal.participant.domain.command.CreateParticipantUser;
import com.thitsaworks.dfsp_portal.usecase.common.CreateNewParticipantUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class CreateNewParticipantUserBean extends CreateNewParticipantUser {

    private static final Logger LOG = LoggerFactory.getLogger(CreateNewParticipantUserBean.class);

    @Autowired
    private CreateParticipantUser createParticipantUser;

    @Autowired
    private CreatePrincipal createPrincipal;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    @Qualifier(PrincipalCache.Strategies.DEFAULT)
    private PrincipalCache principalCache;

    @Override
    @WriteTransactional
    public Output onExecute(CreateNewParticipantUser.Input input) throws Exception {

        SecurityContext securityContext = (SecurityContext) UseCaseContext.get();

        PrincipalData principalData =
                this.principalCache.get(new AccessKey(Long.parseLong(securityContext.getAccessKey())));

        if (principalData == null) {

            throw new PrincipalNotFoundException();

        } else {

            if (principalData.getRealmId() != null &&
                    !principalData.getRealmId().getId().equals(input.getParticipantId().getId())) {

                throw new UnauthorizedCreationException();
            }
        }

        CreateParticipantUser.Output output = this.createParticipantUser.execute(
                new CreateParticipantUser.Input(input.getName(), input.getEmail(), input.getParticipantId(),
                        input.getFirstName(), input.getLastName(), input.getJobTitle()));

        CreatePrincipal.Output createPrincipalOutput = this.createPrincipal.execute(
                new CreatePrincipal.Input(new PrincipalId(output.getParticipantUserId().getId()), input.getRealmType(),
                        input.getPassword(), new RealmId(input.getParticipantId().getId()), input.getUserRoleType(),input.getActiveStatus()));

        return new Output(output.isCreated());
    }

    @Override
    protected String getName() {

        return CreateNewParticipantUser.class.getCanonicalName();
    }

    @Override
    protected String getDescription() {

        return null;
    }

    @Override
    protected String getScope() {

        return "uc_common";
    }

    @Override
    protected String getId() {

        return CreateNewParticipantUser.class.getName();
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
            case SUPERUSER:
            case OPERATION:
            case REPORTING:
                return false;
        }

        return false;
    }

    @Override
    public void onAudit(CreateNewParticipantUser.Input input, CreateNewParticipantUser.Output output)
            throws UserNotFoundException {

        SecurityContext securityContext = (SecurityContext) UseCaseContext.get();

        Auditor.audit(this.objectMapper, CreateNewParticipantUser.class, input, output,
                new UserId(Long.valueOf(securityContext.getUserId())));
    }

}
