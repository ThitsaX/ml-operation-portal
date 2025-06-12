package com.thitsaworks.operation_portal.usecase.hub_operator.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.audit.domain.Auditor;
import com.thitsaworks.operation_portal.audit.identity.UserId;
import com.thitsaworks.operation_portal.component.exception.DFSPPortalException;
import com.thitsaworks.operation_portal.component.security.SecurityContext;
import com.thitsaworks.operation_portal.component.usecase.UseCaseContext;
import com.thitsaworks.operation_portal.component.misc.persistence.transactional.DfspWriteTransactional;
import com.thitsaworks.operation_portal.iam.exception.PrincipalNotFoundException;
import com.thitsaworks.operation_portal.iam.exception.UnauthorizedCreationException;
import com.thitsaworks.operation_portal.iam.identity.AccessKey;
import com.thitsaworks.operation_portal.iam.query.cache.PrincipalCache;
import com.thitsaworks.operation_portal.iam.query.data.PrincipalData;
import com.thitsaworks.operation_portal.usecase.hub_operator.ModifyParticipantShortName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ModifyParticipantShortNameBean extends ModifyParticipantShortName {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyParticipantShortNameBean.class);

    @Autowired
    private ModifyParticipantCompanyShortName modifyParticipant;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    @Qualifier(PrincipalCache.Strategies.DEFAULT)
    private PrincipalCache principalCache;

    @Override
    @DfspWriteTransactional
    public Output onExecute(Input input) throws Exception {

        PrincipalData principalData = this.principalCache.get(input.getAccessKey());

        if (principalData == null) {

            throw new PrincipalNotFoundException();

        } else {

            if (principalData.getRealmId() != null &&
                    !principalData.getRealmId().getId().equals(input.getParticipantId().getId())) {

                throw new UnauthorizedCreationException();
            }
        }

        ModifyParticipantCompanyShortName.Output output = this.modifyParticipant.execute(
                new ModifyParticipantCompanyShortName.Input(input.getParticipantId(),input.getCompanyShortName()));


        return new Output(output.isModified(), output.getParticipantId());
    }

    @Override
    protected String getName() {

        return ModifyParticipantCompanyShortName.class.getCanonicalName();
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

        return ModifyParticipantCompanyShortName.class.getName();
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
    public void onAudit(Input input, Output output) throws DFSPPortalException {
        SecurityContext securityContext = (SecurityContext) UseCaseContext.get();

        Auditor.audit(this.objectMapper, ModifyParticipantCompanyShortName.class, input, output,
                new UserId(Long.valueOf(securityContext.getUserId())));
    }
}
