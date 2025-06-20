package com.thitsaworks.operation_portal.usecase.common.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.misc.security.SecurityContext;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCaseContext;
import com.thitsaworks.operation_portal.core.audit.exception.UserNotFoundException;
import com.thitsaworks.operation_portal.core.audit.model.Auditor;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.data.PrincipalData;
import com.thitsaworks.operation_portal.core.participant.command.CreateContact;
import com.thitsaworks.operation_portal.usecase.common.CreateNewContact;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateNewContactHandler extends CreateNewContact {

    private static final Logger LOG = LoggerFactory.getLogger(CreateNewContactHandler.class);

    private final CreateContact createContact;

    private final PrincipalCache principalCache;

    private ObjectMapper objectMapper;

    @Override
    public Output onExecute(CreateNewContact.Input input) throws Exception {

        for (Input.ContactInfo contactInfo : input.contactInfoList()) {

            this.createContact.execute(new CreateContact.Input(contactInfo.name(),
                                                               contactInfo.title(),
                                                               contactInfo.email(),
                                                               contactInfo.mobile(),
                                                               input.participantId(),
                                                               contactInfo.contactType()));
        }

        return new CreateNewContact.Output(true);
    }

    @Override
    protected String getName() {

        return CreateNewContactHandler.class.getCanonicalName();
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

        return CreateNewContactHandler.class.getName();
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
    public void onAudit(CreateNewContact.Input input, CreateNewContact.Output output) throws UserNotFoundException {

        SecurityContext securityContext = (SecurityContext) UseCaseContext.get();

        Auditor.audit(this.objectMapper, CreateNewContact.class,
                      input, output, new UserId(securityContext.userId()),
                      securityContext.realmId() == null ? null : new RealmId(securityContext.realmId()));
    }

}
