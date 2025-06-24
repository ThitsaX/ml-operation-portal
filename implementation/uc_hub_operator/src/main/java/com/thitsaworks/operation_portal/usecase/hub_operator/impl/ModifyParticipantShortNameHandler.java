package com.thitsaworks.operation_portal.usecase.hub_operator.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.misc.exception.OperationPortalException;
import com.thitsaworks.operation_portal.component.misc.security.SecurityContext;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCaseContext;
import com.thitsaworks.operation_portal.core.audit.model.Auditor;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.data.PrincipalData;
import com.thitsaworks.operation_portal.core.iam.exception.PrincipalNotFoundException;
import com.thitsaworks.operation_portal.core.iam.exception.UnauthorizedCreationException;
import com.thitsaworks.operation_portal.core.participant.command.ModifyParticipantCompanyShortName;
import com.thitsaworks.operation_portal.usecase.hub_operator.ModifyParticipantShortName;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModifyParticipantShortNameHandler extends ModifyParticipantShortName {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyParticipantShortNameHandler.class);

    private final ModifyParticipantCompanyShortName modifyParticipant;

    private final ObjectMapper objectMapper;

    private final PrincipalCache principalCache;

    @Override
    public Output onExecute(Input input) throws Exception {

        PrincipalData principalData = this.principalCache.get(input.accessKey());

        if (principalData == null) {

            throw new PrincipalNotFoundException();

        } else {

            if (principalData.realmId() != null &&
                    !principalData.realmId().getId().equals(input.participantId().getId())) {

                throw new UnauthorizedCreationException();
            }
        }

        ModifyParticipantCompanyShortName.Output output = this.modifyParticipant.execute(
                new ModifyParticipantCompanyShortName.Input(input.participantId(), input.companyShortName()));

        return new Output(output.modified(), output.participantId());
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
                this.principalCache.get(new AccessKey(securityContext.accessKey()));

        return switch (principalData.userRoleType()) {
            case OPERATION, ADMIN -> true;
            case SUPERUSER, REPORTING -> false;
        };

    }


    @Override
    public void onAudit(Input input, Output output) throws OperationPortalException {
        SecurityContext securityContext = (SecurityContext) UseCaseContext.get();

        Auditor.audit(this.objectMapper, ModifyParticipantCompanyShortName.class, input, output,
                      new UserId(securityContext.userId()),
                      securityContext.realmId() == null ? null : new RealmId(securityContext.realmId()));
    }
}
