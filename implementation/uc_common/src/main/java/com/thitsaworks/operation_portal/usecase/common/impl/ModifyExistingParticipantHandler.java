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
import com.thitsaworks.operation_portal.core.iam.exception.PrincipalNotFoundException;
import com.thitsaworks.operation_portal.core.iam.exception.UnauthorizedCreationException;
import com.thitsaworks.operation_portal.core.participant.command.ModifyParticipant;
import com.thitsaworks.operation_portal.usecase.common.ModifyExistingParticipant;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ModifyExistingParticipantHandler extends ModifyExistingParticipant {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyExistingParticipantHandler.class);

    private final ModifyParticipant modifyParticipant;

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

        ModifyParticipant.Output output = this.modifyParticipant.execute(
                new ModifyParticipant.Input(input.participantId(),
                                            input.companyName(),
                                            input.address(),
                                            input.mobile(),
                                            input.contactInfoList()
                                                 .stream()
                                                 .map(info -> new ModifyParticipant.Input.ContactInfo(info.contactId(),
                                                                                                      info.name(),
                                                                                                      info.title(),
                                                                                                      info.email(),
                                                                                                      info.mobile(),
                                                                                                      info.contactType()))
                                                 .collect(Collectors.toList()),
                                            input.liquidityProfileInfoList()
                                                 .stream()
                                                 .map(info -> new ModifyParticipant.Input.LiquidityProfileInfo(info.liquidityProfileId(),
                                                                                                               info.accountName(),
                                                                                                               info.accountNumber(),
                                                                                                               info.currency(),
                                                                                                               info.isActive()))
                                                 .collect(Collectors.toList())));

        return new ModifyExistingParticipant.Output(output.modified(), output.participantId());
    }

    @Override
    protected String getName() {

        return ModifyExistingParticipant.class.getCanonicalName();
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

        return ModifyExistingParticipant.class.getName();
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
    public void onAudit(ModifyExistingParticipant.Input input, ModifyExistingParticipant.Output output)
            throws UserNotFoundException {

        SecurityContext securityContext = (SecurityContext) UseCaseContext.get();

        Auditor.audit(this.objectMapper, ModifyExistingParticipant.class, input, output,
                      new UserId(securityContext.userId()),
                      securityContext.realmId() == null ? null : new RealmId(securityContext.realmId()));
    }

}
