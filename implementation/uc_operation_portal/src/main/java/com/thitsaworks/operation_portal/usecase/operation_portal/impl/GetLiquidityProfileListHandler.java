package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.data.LiquidityProfileData;
import com.thitsaworks.operation_portal.core.participant.query.LiquidityProfileQuery;
import com.thitsaworks.operation_portal.core.participant.query.ParticipantQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetLiquidityProfileList;
import com.thitsaworks.operation_portal.usecase.util.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class GetLiquidityProfileListHandler
    extends OperationPortalAuditableUseCase<GetLiquidityProfileList.Input, GetLiquidityProfileList.Output>
    implements GetLiquidityProfileList {

    private static final Logger LOG = LoggerFactory.getLogger(GetLiquidityProfileListHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.OPERATION,
                                                                    UserRoleType.ADMIN);

    private final LiquidityProfileQuery liquidityProfileQuery;

    private final ParticipantQuery participantQuery;

    public GetLiquidityProfileListHandler(CreateInputAuditCommand createInputAuditCommand,
                                          CreateOutputAuditCommand createOutputAuditCommand,
                                          CreateExceptionAuditCommand createExceptionAuditCommand,
                                          ObjectMapper objectMapper,
                                          PrincipalCache principalCache,
                                          ActionAuthorizationManager actionAuthorizationManager,
                                          LiquidityProfileQuery liquidityProfileQuery,
                                          ParticipantQuery participantQuery) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.liquidityProfileQuery = liquidityProfileQuery;
        this.participantQuery = participantQuery;

    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        var participant = this.participantQuery.get(input.participantId());

        List<LiquidityProfileData>
            liquidityProfileDataList =
            this.liquidityProfileQuery.getActiveLiquidityProfiles(participant.participantId());

        List<Output.LiquidityProfileInfo> liquidityProfileInfoList = new ArrayList<>();

        for (LiquidityProfileData liquidityProfileData : liquidityProfileDataList) {

            liquidityProfileInfoList.add(
                new Output.LiquidityProfileInfo(liquidityProfileData.liquidityProfileId(),
                                                liquidityProfileData.bankName(),
                                                liquidityProfileData.accountName(),
                                                liquidityProfileData.accountNumber(),
                                                liquidityProfileData.currency(),
                                                liquidityProfileData.isActive()));
        }

        return new Output(liquidityProfileInfoList);
    }

}
