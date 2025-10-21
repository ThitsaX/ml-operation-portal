package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.scheduler.command.ModifySchedulerConfigCommand;
import com.thitsaworks.operation_portal.core.scheduler.data.SchedulerConfigData;
import com.thitsaworks.operation_portal.core.scheduler.query.SchedulerConfigQuery;
import com.thitsaworks.operation_portal.core.settlement.data.SettlementModelData;
import com.thitsaworks.operation_portal.core.settlement.query.SettlementModelQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetSettlementSchedulerList;
import com.thitsaworks.operation_portal.usecase.operation_portal.scheduler.SchedulerEngine;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetSettlementSchedulerListHandler
        extends OperationPortalAuditableUseCase<GetSettlementSchedulerList.Input, GetSettlementSchedulerList.Output>
        implements GetSettlementSchedulerList {

    private static final Logger LOG = LoggerFactory.getLogger(GetSettlementSchedulerListHandler.class);

    private final ModifySchedulerConfigCommand modifySchedulerConfigCommand;

    private final SettlementModelQuery settlementModelQuery;

    private final SchedulerConfigQuery schedulerConfigQuery;

    private final SchedulerEngine schedulerEngine;

    public GetSettlementSchedulerListHandler(CreateInputAuditCommand createInputAuditCommand,
                                             CreateOutputAuditCommand createOutputAuditCommand,
                                             CreateExceptionAuditCommand createExceptionAuditCommand,
                                             SettlementModelQuery settlementModelQuery,
                                             SchedulerConfigQuery schedulerConfigQuery,
                                             SchedulerEngine schedulerEngine,
                                             ObjectMapper objectMapper,
                                             PrincipalCache principalCache,
                                             ModifySchedulerConfigCommand modifySchedulerConfigCommand,
                                             ActionAuthorizationManager actionAuthorizationManager) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.modifySchedulerConfigCommand = modifySchedulerConfigCommand;
        this.settlementModelQuery = settlementModelQuery;
        this.schedulerConfigQuery = schedulerConfigQuery;
        this.schedulerEngine = schedulerEngine;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        SettlementModelData settlementModelData = this.settlementModelQuery.get(input.settlementModelId());

        List<SchedulerConfigData> schedulerConfigDataList = this.schedulerConfigQuery.getSchedulerConfigs(null);

        List<SchedulerConfigData> settlementSchedulerList =
                schedulerConfigDataList.stream()
                                       .filter(schedulerConfigData -> settlementModelData.schedulerConfigIds()
                                                                                         .contains(
                                                                                                 schedulerConfigData.schedulerConfigId()))
                                       .toList();

        return new Output(settlementSchedulerList);
    }

}
