package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.hub_services.data.FinancialData;
import com.thitsaworks.operation_portal.core.hub_services.query.GetParticipantPositionsDataQuery;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.cache.ParticipantCache;
import com.thitsaworks.operation_portal.core.participant.cache.UserCache;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantData;
import com.thitsaworks.operation_portal.core.participant.data.UserData;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantErrors;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;
import com.thitsaworks.operation_portal.core.participant.model.ParticipantNDC;
import com.thitsaworks.operation_portal.core.participant.query.ParticipantNDCQuery;
import com.thitsaworks.operation_portal.core.participant.query.ParticipantQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetParticipantPositionList;
import com.thitsaworks.operation_portal.usecase.util.UserPermissionManager;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GetParticipantPositionListHandler
    extends OperationPortalUseCase<GetParticipantPositionList.Input, GetParticipantPositionList.Output>
    implements GetParticipantPositionList {

    private static final Logger LOG = LoggerFactory.getLogger(GetParticipantPositionListHandler.class);

    private static final String allDfsp = "All";

    private static final BigDecimal roundingValue = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

    private final GetParticipantPositionsDataQuery getParticipantPositionsDataQuery;

    private final ParticipantCache participantCache;

    private final UserCache userCache;

    private final ParticipantNDCQuery participantNDCQuery;

    private final UserPermissionManager userPermissionManager;

    private final ParticipantQuery participantQuery;

    public GetParticipantPositionListHandler(PrincipalCache principalCache,
                                             GetParticipantPositionsDataQuery getParticipantPositionsDataQuery,
                                             ParticipantCache participantCache,
                                             UserCache userCache,
                                             ParticipantNDCQuery participantNDCQuery,
                                             ActionAuthorizationManager actionAuthorizationManager,
                                             UserPermissionManager userPermissionManager,
                                             ParticipantQuery participantQuery) {

        super(principalCache, actionAuthorizationManager);

        this.getParticipantPositionsDataQuery = Objects.requireNonNull(getParticipantPositionsDataQuery);
        this.participantCache = Objects.requireNonNull(participantCache);
        this.userCache = Objects.requireNonNull(userCache);
        this.participantNDCQuery = Objects.requireNonNull(participantNDCQuery);
        this.userPermissionManager = Objects.requireNonNull(userPermissionManager);
        this.participantQuery = Objects.requireNonNull(participantQuery);
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        final UserData userData = userCache.get(input.userId());
        if (userData == null) {
            throw new ParticipantException(ParticipantErrors.USER_NOT_FOUND.format(input.userId()
                                                                                        .getId()
                                                                                        .toString()));
        }

        final ParticipantData userParticipant = participantCache.get(userData.participantId());

        if (userParticipant == null) {
            throw new ParticipantException(ParticipantErrors.PARTICIPANT_NOT_FOUND.format(userData.participantId()
                                                                                                  .getId()
                                                                                                  .toString()));
        }

        final boolean isDfspUser = userPermissionManager.isDfsp(new PrincipalId(input.userId()
                                                                                     .getId()));
        final String fspName = isDfspUser
                                   ? userParticipant.participantName()
                                                    .getValue()
                                   : allDfsp;

        final GetParticipantPositionsDataQuery.Output output =
            getParticipantPositionsDataQuery.execute(new GetParticipantPositionsDataQuery.Input(fspName));

        final var
            rows =
            Optional.ofNullable(output.getFinancialData())
                    .orElseGet(List::of);
        final List<FinancialData> result = new ArrayList<>(rows.size());

        final Map<String, ParticipantData> participantDescCache = new ConcurrentHashMap<>();

        for (var dto : rows) {

            final BigDecimal ndcPercent = participantNDCQuery.get(dto.dfspId(), dto.currency())
                                                             .map(ParticipantNDC::getNdcPercent)
                                                             .map(v -> v.setScale(2, RoundingMode.HALF_UP))
                                                             .orElse(roundingValue);

            final ParticipantData resolved = isDfspUser
                                                 ? userParticipant
                                                 : participantDescCache.computeIfAbsent(dto.dfspId(),
                                                                                        id -> resolveParticipantDescription(
                                                                                            id));

            final ParticipantId participantId = resolved.participantId();

            final String displayName = isDfspUser ? userParticipant.description() : resolved.description();

            final FinancialData updated = new FinancialData(
                participantId,
                dto.dfspId(),
                displayName,
                dto.currency(),
                dto.balance(),
                dto.currentPosition(),
                ndcPercent,
                dto.ndc(),
                dto.ndcUsed(),
                dto.participantSettlementCurrencyId(),
                dto.participantPositionCurrencyId(),
                dto.isActive()
            );

            result.add(updated);
        }

        return new GetParticipantPositionList.Output(result);
    }

    private ParticipantData resolveParticipantDescription(String dfspId) {

        try {

            return participantQuery.get(dfspId)
                                   .orElseGet(() -> {
                                       LOG.warn("Participant not found for dfspId={}", dfspId);
                                       return unknownParticipant(dfspId);
                                   });
        } catch (Exception ex) {

            LOG.error("Error resolving participant for dfspId={}", dfspId, ex);
            return unknownParticipant(dfspId);
        }
    }

    private ParticipantData unknownParticipant(String dfspId) {

        return new ParticipantData(
            new ParticipantId(1L),
            "",
            null,
            "",
            null,
            null,
            null,
            null,
            null,
            null
        );
    }

}
