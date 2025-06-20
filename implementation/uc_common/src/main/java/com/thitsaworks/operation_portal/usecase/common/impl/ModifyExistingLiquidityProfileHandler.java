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
import com.thitsaworks.operation_portal.core.participant.command.ModifyLiquidityProfile;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantNotFoundException;
import com.thitsaworks.operation_portal.usecase.common.ModifyExistingLiquidityProfile;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModifyExistingLiquidityProfileHandler extends ModifyExistingLiquidityProfile {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyExistingLiquidityProfileHandler.class);

    private final ModifyLiquidityProfile modifyLiquidityProfile;

    private final PrincipalCache principalCache;

    private final ParticipantCache participantCache;

    private final ObjectMapper objectMapper;

    @Override
    public Output onExecute(Input input) throws Exception {

        if (this.participantCache.get(input.participantId()) == null) {

            throw new ParticipantNotFoundException(input.participantId().getId().toString());
        }

        for (ModifyExistingLiquidityProfile.Input.LiquidityProfileInfo profileInfo : input.liquidityProfileInfoList()) {

            this.modifyLiquidityProfile.execute(
                    new ModifyLiquidityProfile.Input(input.participantId(),
                                                     profileInfo.liquidityProfileId(),
                                                     profileInfo.accountName(),
                                                     profileInfo.accountNumber(),
                                                     profileInfo.currency(),
                                                     profileInfo.isActive()));
        }

        return new Output(true);
    }

    @Override
    protected String getName() {

        return ModifyExistingLiquidityProfile.class.getCanonicalName();
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

        return ModifyExistingLiquidityProfile.class.getName();
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
    public void onAudit(ModifyExistingLiquidityProfile.Input input, ModifyExistingLiquidityProfile.Output output)
            throws UserNotFoundException {

        SecurityContext securityContext = (SecurityContext) UseCaseContext.get();

        Auditor.audit(this.objectMapper, ModifyExistingLiquidityProfile.class, input, output,
                      new UserId(securityContext.userId()),
                      securityContext.realmId() == null ? null : new RealmId(securityContext.realmId()));
    }

}
