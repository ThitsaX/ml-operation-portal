package com.thitsaworks.operation_portal.usecase.common.impl;

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
import com.thitsaworks.operation_portal.participant.domain.command.CreateContact;
import com.thitsaworks.operation_portal.usecase.common.CreateNewContact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class CreateNewContactBean extends CreateNewContact {

    private static final Logger LOG = LoggerFactory.getLogger(CreateNewContactBean.class);

    @Autowired
    private CreateContact createContact;

    @Autowired
    @Qualifier(PrincipalCache.Strategies.DEFAULT)
    private PrincipalCache principalCache;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @DfspWriteTransactional
    public CreateNewContact.Output onExecute(CreateNewContact.Input input) throws Exception {

        for (Input.ContactInfo contactInfo : input.getContactInfoList()) {

            this.createContact.execute(new CreateContact.Input(contactInfo.getName(), contactInfo.getTitle(),
                    contactInfo.getEmail(), contactInfo.getMobile(), input.getParticipantId(),
                    contactInfo.getContactType()));
        }

        return new CreateNewContact.Output(true);
    }

    @Override
    protected String getName() {

        return CreateNewContactBean.class.getCanonicalName();
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

        return CreateNewContactBean.class.getName();
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
            
            case OPERATION:
            case ADMIN:
                return true;
            case SUPERUSER:
            case REPORTING:
                return false;
        }

        return false;

    }

    @Override
    public void onAudit(CreateNewContact.Input input, CreateNewContact.Output output) throws UserNotFoundException {

        SecurityContext securityContext = (SecurityContext) UseCaseContext.get();

        Auditor.audit(this.objectMapper, CreateNewContact.class, input, output,
                new UserId(Long.valueOf(securityContext.getUserId())));
    }

}
