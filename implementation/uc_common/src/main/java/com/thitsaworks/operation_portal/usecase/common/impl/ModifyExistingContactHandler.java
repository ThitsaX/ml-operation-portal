package com.thitsaworks.operation_portal.usecase.common.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCaseContext;
import com.thitsaworks.operation_portal.component.misc.security.SecurityContext;
import com.thitsaworks.operation_portal.core.audit.exception.UserNotFoundException;
import com.thitsaworks.operation_portal.core.audit.model.Auditor;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.data.PrincipalData;
import com.thitsaworks.operation_portal.core.participant.cache.ParticipantCache;
import com.thitsaworks.operation_portal.core.participant.command.ModifyContact;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantNotFoundException;
import com.thitsaworks.operation_portal.usecase.common.ModifyExistingContact;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModifyExistingContactHandler extends ModifyExistingContact {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyExistingContactHandler.class);

    private final ModifyContact modifyContact;

    private final PrincipalCache principalCache;

    private final ParticipantCache participantCache;

    private final ObjectMapper objectMapper;

    @Override
    public Output onExecute(Input input) throws Exception {

        if (this.participantCache.get(input.participantId()) == null) {

            throw new ParticipantNotFoundException(input.participantId().getId().toString());
        }

        for (Input.ContactInfo contactInfo : input.contactInfoList()) {

            this.modifyContact.execute(new ModifyContact.Input(input.participantId(),
                                                               contactInfo.contactId(),
                                                               contactInfo.name(),
                                                               contactInfo.title(),
                                                               contactInfo.email(),
                                                               contactInfo.mobile()));
        }

        return new ModifyExistingContact.Output(true);
    }

    @Override
    protected String getName() {

        return ModifyExistingContactHandler.class.getCanonicalName();
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

        return ModifyExistingContactHandler.class.getName();
    }

    @Override
    public boolean isOwned(Object userDetails) {

        return true;
    }

    @Override
    public boolean isAuthorized(Object userDetails) {

        SecurityContext securityContext = (SecurityContext) userDetails;

        PrincipalData principalData =
                this.principalCache.get(new AccessKey(securityContext.accessKey()));

        return switch (principalData.userRoleType()) {
            case OPERATION, ADMIN -> true;
            case SUPERUSER, REPORTING -> false;
        };

    }

    @Override
    public void onAudit(ModifyExistingContact.Input input, ModifyExistingContact.Output output)
            throws UserNotFoundException {

        SecurityContext securityContext = (SecurityContext) UseCaseContext.get();

        Auditor.audit(this.objectMapper, ModifyExistingContact.class, input, output,
                      new UserId(securityContext.userId()),
                      securityContext.realmId() == null ? null : new RealmId(securityContext.realmId()));
    }

}
