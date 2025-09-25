package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.hub_services.SettlementHubClient;
import com.thitsaworks.operation_portal.core.hub_services.support.Settlement;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetSettlementList;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.ConnectException;
import java.util.List;

@Service
public class GetSettlementListHandler
    extends OperationPortalUseCase<GetSettlementList.Input, GetSettlementList.Output>
        implements GetSettlementList {

    private static final Logger LOG = LoggerFactory.getLogger(GetSettlementListHandler.class);

    private final SettlementHubClient settlementHubClient;

    public GetSettlementListHandler(PrincipalCache principalCache,
                                    ActionAuthorizationManager actionAuthorizationManager,
                                    SettlementHubClient settlementHubClient) {

        super(principalCache,
              actionAuthorizationManager);

        this.settlementHubClient = settlementHubClient;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException, ConnectException {

        List<Settlement> settlementList = this.settlementHubClient.getSettlementList(
                input.currency(),
                input.participantId(),
                input.settlementWindowId(),
                input.accountId(),
                input.state(),
                input.fromDateTime(),
                input.toDateTime(),
                input.fromSettlementWindowDateTime(),
                input.toSettlementWindowDateTime());

        return new Output(settlementList);
    }

}
