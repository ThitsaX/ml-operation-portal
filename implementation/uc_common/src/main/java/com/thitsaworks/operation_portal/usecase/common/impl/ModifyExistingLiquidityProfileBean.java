package com.thitsaworks.operation_portal.usecase.common.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.audit.domain.Auditor;
import com.thitsaworks.operation_portal.audit.exception.UserNotFoundException;
import com.thitsaworks.operation_portal.audit.identity.UserId;
import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.component.security.SecurityContext;
import com.thitsaworks.operation_portal.component.usecase.UseCaseContext;
import com.thitsaworks.operation_portal.iam.identity.AccessKey;
import com.thitsaworks.operation_portal.iam.query.cache.PrincipalCache;
import com.thitsaworks.operation_portal.iam.query.data.PrincipalData;
import com.thitsaworks.operation_portal.participant.domain.command.ModifyLiquidityProfile;
import com.thitsaworks.operation_portal.participant.exception.ParticipantNotFoundException;
import com.thitsaworks.operation_portal.participant.query.cache.ParticipantCache;
import com.thitsaworks.operation_portal.usecase.common.ModifyExistingLiquidityProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ModifyExistingLiquidityProfileBean extends ModifyExistingLiquidityProfile {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyExistingLiquidityProfileBean.class);

    @Autowired
    private ModifyLiquidityProfile modifyLiquidityProfile;

    @Autowired
    @Qualifier(PrincipalCache.Strategies.DEFAULT)
    private PrincipalCache principalCache;

    @Autowired
    @Qualifier(ParticipantCache.Strategies.DEFAULT)
    private ParticipantCache participantCache;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @CoreWriteTransactional
    public Output onExecute(Input input) throws Exception {

        if (this.participantCache.get(input.getParticipantId()) == null) {

            throw new ParticipantNotFoundException(input.getParticipantId().getId().toString());
        }

        for (ModifyExistingLiquidityProfile.Input.LiquidityProfileInfo profileInfo : input.getLiquidityProfileInfoList()) {

            this.modifyLiquidityProfile.execute(
                    new ModifyLiquidityProfile.Input(input.getParticipantId(), profileInfo.getLiquidityProfileId(),
                            profileInfo.getAccountName(), profileInfo.getAccountNumber(), profileInfo.getCurrency(),
                            profileInfo.getIsActive()));
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
    public void onAudit(ModifyExistingLiquidityProfile.Input input, ModifyExistingLiquidityProfile.Output output)
            throws UserNotFoundException {

        SecurityContext securityContext = (SecurityContext) UseCaseContext.get();

        Auditor.audit(this.objectMapper, ModifyExistingLiquidityProfile.class, input, output,
                new UserId(Long.valueOf(securityContext.getUserId())));
    }

}
