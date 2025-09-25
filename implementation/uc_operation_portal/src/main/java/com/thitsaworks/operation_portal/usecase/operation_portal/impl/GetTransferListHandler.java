package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.hub_services.data.TransferData;
import com.thitsaworks.operation_portal.core.hub_services.query.GetTransfersQuery;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.cache.ParticipantCache;
import com.thitsaworks.operation_portal.core.participant.cache.UserCache;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantData;
import com.thitsaworks.operation_portal.core.participant.data.UserData;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantErrors;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetTransferList;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetTransferListHandler
    extends OperationPortalUseCase<GetTransferList.Input, GetTransferList.Output>
    implements GetTransferList {

    private static final Logger LOG = LoggerFactory.getLogger(GetTransferListHandler.class);

    private final GetTransfersQuery getTransfersQuery;

    private final ParticipantCache participantCache;

    private final UserCache userCache;

    public GetTransferListHandler(PrincipalCache principalCache,
                                  ActionAuthorizationManager actionAuthorizationManager,
                                  GetTransfersQuery getTransfersQuery,
                                  ParticipantCache participantCache,
                                  UserCache userCache) {

        super(principalCache,
              actionAuthorizationManager);

        this.getTransfersQuery = getTransfersQuery;
        this.participantCache = participantCache;
        this.userCache = userCache;

    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        UserData userData = this.userCache.get(input.userId());

        if (userData == null) {

            throw new ParticipantException(ParticipantErrors.USER_NOT_FOUND.format(input.userId()
                                                                                        .getId().toString()));
        }

        ParticipantData participantData = this.participantCache.get(userData.participantId());

        if (participantData == null) {

            throw new ParticipantException(ParticipantErrors.PARTICIPANT_NOT_FOUND
                                               .format(userData.participantId()
                                                               .getId().toString()));
        }

        String
            fspName =
            participantData.participantName()
                           .getValue();

        GetTransfersQuery.Output output = this.getTransfersQuery.execute(new GetTransfersQuery.Input(input.fromDate(),
                                                                                                     input.toDate(),
                                                                                                     input.transferId(),
                                                                                                     input.payerFspId(),
                                                                                                     input.payeeFspId(),
                                                                                                     input.payerIdentifierTypeId(),
                                                                                                     input.payeeIdentifierTypeId(),
                                                                                                     input.payerIdentifierValue(),
                                                                                                     input.payeeIdentifierValue(),
                                                                                                     input.currencyId(),
                                                                                                     input.transferStateId(),
                                                                                                     fspName,
                                                                                                     input.timeZone()));

        List<TransferData> transferDataList = new ArrayList<>();

        for (TransferData data : output.getTransferInfoList()) {

            transferDataList.add(
                new TransferData(
                    data.getTransferId(),
                    data.getState(),
                    data.getType(),
                    data.getCurrency(),
                    data.getAmount(),
                    data.getPayerDfsp(),
                    data.getPayeeDfsp(),
                    data.getWindowId(),
                    data.getSettlementBatch(),
                    data.getSubmittedOnDate()));
        }

        return new GetTransferList.Output(transferDataList);

    }

}
