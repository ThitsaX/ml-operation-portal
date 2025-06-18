package com.thitsaworks.operation_portal.usecase.common.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCaseContext;
import com.thitsaworks.operation_portal.component.security.SecurityContext;
import com.thitsaworks.operation_portal.core.audit.exception.UserNotFoundException;
import com.thitsaworks.operation_portal.core.audit.model.Auditor;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.data.PrincipalData;
import com.thitsaworks.operation_portal.core.participant.command.CreateLiquidityProfile;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantNotFoundException;
import com.thitsaworks.operation_portal.usecase.common.CreateNewLiquidityProfile;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateNewLiquidityProfileBean extends CreateNewLiquidityProfile {

    private static final Logger LOG = LoggerFactory.getLogger(CreateNewLiquidityProfileBean.class);

    private CreateLiquidityProfile createLiquidityProfile;

    private PrincipalCache principalCache;

    private ObjectMapper objectMapper;

    @Override
    public Output onExecute(Input input) throws ParticipantNotFoundException {

        for (CreateNewLiquidityProfile.Input.LiquidityProfileInfo profileInfo : input.liquidityProfileInfoList()) {

            this.createLiquidityProfile.execute(
                    new CreateLiquidityProfile.Input(input.participantId(),
                                                     profileInfo.accountName(),
                                                     profileInfo.accountNumber(),
                                                     profileInfo.currency(),
                                                     profileInfo.isActive()));

        }

        return new Output(true);
    }

    @Override
    protected String getName() {

        return CreateNewLiquidityProfile.class.getCanonicalName();
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

        return CreateNewLiquidityProfile.class.getName();
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

        return switch (principalData.userRoleType()) {
            case OPERATION, ADMIN -> true;
            case SUPERUSER, REPORTING -> false;
        };

    }

    @Override
    public void onAudit(CreateNewLiquidityProfile.Input input, CreateNewLiquidityProfile.Output output)
            throws UserNotFoundException {

        SecurityContext securityContext = (SecurityContext) UseCaseContext.get();

        Auditor.audit(this.objectMapper, CreateNewLiquidityProfile.class, input, output,
                      new UserId(Long.valueOf(securityContext.getUserId())));
    }

}
