package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.hub_services.data.SettlementWindowInfoData;
import com.thitsaworks.operation_portal.core.hub_services.query.GetNetTransferAmountBySettlementIdQuery;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetNetTransferAmountBySettlementId;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetNetTransferAmountBySettlementIdHandler
    extends OperationPortalUseCase<GetNetTransferAmountBySettlementId.Input, GetNetTransferAmountBySettlementId.Output>
    implements GetNetTransferAmountBySettlementId {

    private static final Logger LOG = LoggerFactory.getLogger(GetNetTransferAmountBySettlementIdHandler.class);

    private final GetNetTransferAmountBySettlementIdQuery getNetTransferAmountBySettlementIdQuery;

    public GetNetTransferAmountBySettlementIdHandler(PrincipalCache principalCache,
                                                     ActionAuthorizationManager actionAuthorizationManager,
                                                     GetNetTransferAmountBySettlementIdQuery getNetTransferAmountBySettlementIdQuery) {

        super(principalCache, actionAuthorizationManager);

        this.getNetTransferAmountBySettlementIdQuery = getNetTransferAmountBySettlementIdQuery;

    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        GetNetTransferAmountBySettlementIdQuery.Output
            output =
            this.getNetTransferAmountBySettlementIdQuery.execute(new GetNetTransferAmountBySettlementIdQuery.Input(input.settlementId()));

        List<Detail> details = new ArrayList<>();

        for (SettlementWindowInfoData windowInfo : output.getWindowInfoList()) {

            Detail detail = new Detail(windowInfo.getDfspName(),
                                       windowInfo.getParticipantLimit(),
                                       windowInfo.getParticipantBalance(),
                                       windowInfo.getDebit(),
                                       windowInfo.getCredit(),
                                       windowInfo.getNdcPercent(),
                                       windowInfo.getCurrencyId(),
                                       windowInfo.getParticipantSettlementCurrencyId());

            details.add(detail);
        }

        String
            windowOpenedDate =
            output.getWindowInfoList()
                  .isEmpty() ? null : output.getWindowInfoList()
                                            .get(0)
                                            .getWindowOpenedDate();

        String
            windowClosedDate =
            output.getWindowInfoList()
                  .isEmpty() ? null : output.getWindowInfoList()
                                            .get(0)
                                            .getWindowClosedDate();

        String
            settlementWindowIds =
            output.getWindowInfoList()
                  .isEmpty() ? null : output.getWindowInfoList()
                                            .get(0)
                                            .getSettlementWindowIds();

        return new Output(input.settlementId(), settlementWindowIds, windowOpenedDate, windowClosedDate, details);

    }

}
