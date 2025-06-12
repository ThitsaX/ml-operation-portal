package com.thitsaworks.operation_portal.usecase.participant.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.audit.domain.Auditor;
import com.thitsaworks.operation_portal.audit.exception.UserNotFoundException;
import com.thitsaworks.operation_portal.audit.identity.UserId;
import com.thitsaworks.operation_portal.component.security.SecurityContext;
import com.thitsaworks.operation_portal.component.usecase.UseCaseContext;
import com.thitsaworks.operation_portal.component.misc.persistence.transactional.DfspWriteTransactional;
import com.thitsaworks.operation_portal.iam.domain.command.ModifyPrincipal;
import com.thitsaworks.operation_portal.iam.exception.PrincipalNotFoundException;
import com.thitsaworks.operation_portal.iam.exception.UnauthorizedCreationException;
import com.thitsaworks.operation_portal.iam.identity.AccessKey;
import com.thitsaworks.operation_portal.iam.identity.PrincipalId;
import com.thitsaworks.operation_portal.iam.query.cache.PrincipalCache;
import com.thitsaworks.operation_portal.iam.query.data.PrincipalData;
import com.thitsaworks.operation_portal.iam.type.PrincipalStatus;
import com.thitsaworks.operation_portal.participant.domain.command.RemoveParticipantUser;
import com.thitsaworks.operation_portal.usecase.participant.RemoveExistingParticipantUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class RemoveExistingParticipantUserBean extends RemoveExistingParticipantUser {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveExistingParticipantUserBean.class);

    @Autowired
    private RemoveParticipantUser removeParticipantUser;

    @Autowired
    private ModifyPrincipal modifyPrincipal;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    @Qualifier(PrincipalCache.Strategies.DEFAULT)
    private PrincipalCache principalCache;

    @Override
    @DfspWriteTransactional
    public RemoveExistingParticipantUser.Output onExecute(RemoveExistingParticipantUser.Input input) throws Exception {

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

        RemoveParticipantUser.Output output = this.removeParticipantUser.execute(
                new RemoveParticipantUser.Input(input.getParticipantId(), input.getParticipantUserId()));

        ModifyPrincipal.Output principalOutput = this.modifyPrincipal.execute(
                new ModifyPrincipal.Input(new PrincipalId(output.getParticipantUserId().getId()),
                        PrincipalStatus.INACTIVE));

        return new RemoveExistingParticipantUser.Output(output.isRemoved(), output.getParticipantUserId());
    }

    @Override
    protected String getName() {

        return RemoveExistingParticipantUser.class.getCanonicalName();
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

        return RemoveExistingParticipantUser.class.getName();
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
            case REPORTING:
            case OPERATION:
                return false;
        }

        return false;
    }

    @Override
    public void onAudit(RemoveExistingParticipantUser.Input input, RemoveExistingParticipantUser.Output output)
            throws UserNotFoundException {

        SecurityContext securityContext = (SecurityContext) UseCaseContext.get();

        Auditor.audit(this.objectMapper, RemoveExistingParticipantUser.class, input, output,
                new UserId(Long.valueOf(securityContext.getUserId())));
    }

}
