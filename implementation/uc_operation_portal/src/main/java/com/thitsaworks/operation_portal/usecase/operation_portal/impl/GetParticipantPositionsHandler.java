package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.hub_services.data.FinancialData;
import com.thitsaworks.operation_portal.core.hub_services.query.GetParticipantPositionsDataQuery;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.cache.ParticipantCache;
import com.thitsaworks.operation_portal.core.participant.cache.ParticipantUserCache;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantData;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantNDCData;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantUserData;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantErrors;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;
import com.thitsaworks.operation_portal.core.participant.query.ParticipantNDCQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetParticipantPositionsData;
import com.thitsaworks.operation_portal.usecase.util.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class GetParticipantPositionsHandler
    extends OperationPortalUseCase<GetParticipantPositionsData.Input, GetParticipantPositionsData.Output>
    implements GetParticipantPositionsData {

    private static final Logger LOG = LoggerFactory.getLogger(GetParticipantPositionsHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.OPERATION,
                                                                    UserRoleType.ADMIN);

    private final GetParticipantPositionsDataQuery getParticipantPositionsDataQuery;

    private final ParticipantCache participantCache;

    private final ParticipantUserCache participantUserCache;

    private final ParticipantNDCQuery participantNDCQuery;

    public GetParticipantPositionsHandler(PrincipalCache principalCache,
                                          GetParticipantPositionsDataQuery getParticipantPositionsDataQuery,
                                          ParticipantCache participantCache,
                                          ParticipantUserCache participantUserCache,
                                          ParticipantNDCQuery participantNDCQuery,
                                          ActionAuthorizationManager actionAuthorizationManager) {

        super(PERMITTED_ROLES, principalCache, actionAuthorizationManager);

        this.getParticipantPositionsDataQuery = getParticipantPositionsDataQuery;
        this.participantCache = participantCache;
        this.participantUserCache = participantUserCache;
        this.participantNDCQuery = participantNDCQuery;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        ParticipantUserData participantUserData = this.participantUserCache.get(input.participantUserId());

        if (participantUserData == null) {

            throw new ParticipantException(ParticipantErrors.USER_NOT_FOUND);
        }

        ParticipantData participantData = this.participantCache.get(participantUserData.participantId());

        if (participantData == null) {

            throw new ParticipantException(ParticipantErrors.PARTICIPANT_NOT_FOUND);
        }

        String
            fspName =
            participantData.participantName()
                           .getValue();

        GetParticipantPositionsDataQuery.Output
            output =
            this.getParticipantPositionsDataQuery.execute(new GetParticipantPositionsDataQuery.Input(fspName));

        List<FinancialData> updatedList = new ArrayList<>();

        for (var dto : output.getFinancialData()) {

            Optional<ParticipantNDCData> participantNDCData = this.participantNDCQuery.get(dto.dfspId(),
                                                                                           dto.currency());

            BigDecimal ndcPercent =
                participantNDCData.map(ParticipantNDCData::ndcPercent)
                                  .orElse(BigDecimal.ZERO)
                                  .setScale(2,
                                            RoundingMode.HALF_UP);

            FinancialData updated = new FinancialData(dto.dfspId(),
                                                      participantData.description(),
                                                      dto.currency(),
                                                      dto.balance(),
                                                      dto.currentPosition(),
                                                      ndcPercent,
                                                      dto.ndc(),
                                                      dto.ndcUsed(),
                                                      dto.participantSettlementCurrencyId(),
                                                      dto.participantPositionCurrencyId());

            updatedList.add(updated);
        }

        return new GetParticipantPositionsData.Output(updatedList);
    }

}
