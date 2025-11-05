package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.scheduler.command.ModifySchedulerConfigCommand;
import com.thitsaworks.operation_portal.core.scheduler.command.impl.ModifySchedulerConfigCommandHandler;
import com.thitsaworks.operation_portal.core.scheduler.data.SchedulerConfigData;
import com.thitsaworks.operation_portal.core.scheduler.query.SchedulerConfigQuery;
import com.thitsaworks.operation_portal.core.settlement.data.SettlementModelData;
import com.thitsaworks.operation_portal.core.settlement.exception.SettlementErrors;
import com.thitsaworks.operation_portal.core.settlement.exception.SettlementException;
import com.thitsaworks.operation_portal.core.settlement.query.SettlementModelQuery;
import com.thitsaworks.operation_portal.core.settlement.query.SettlementSchedulerQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.ModifySettlementScheduler;
import com.thitsaworks.operation_portal.usecase.operation_portal.scheduler.SchedulerEngine;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModifySettlementSchedulerHandler
        extends OperationPortalAuditableUseCase<ModifySettlementScheduler.Input, ModifySettlementScheduler.Output>
        implements ModifySettlementScheduler {

    private static final Logger LOG = LoggerFactory.getLogger(ModifySettlementSchedulerHandler.class);

    private final ModifySchedulerConfigCommand modifySchedulerConfigCommand;

    private final SettlementModelQuery settlementModelQuery;

    private final SettlementSchedulerQuery settlementSchedulerQuery;

    private final SchedulerConfigQuery schedulerConfigQuery;

    private final SchedulerEngine schedulerEngine;

    public ModifySettlementSchedulerHandler(CreateInputAuditCommand createInputAuditCommand,
                                            CreateOutputAuditCommand createOutputAuditCommand,
                                            CreateExceptionAuditCommand createExceptionAuditCommand,
                                            SettlementModelQuery settlementModelQuery,
                                            SettlementSchedulerQuery settlementSchedulerQuery,
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
        this.settlementSchedulerQuery = settlementSchedulerQuery;
        this.schedulerConfigQuery = schedulerConfigQuery;
        this.schedulerEngine = schedulerEngine;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        SettlementModelData settlementModelData = this.settlementModelQuery.get(input.settlementModelId());

        SchedulerConfigData settlementSchedulerData = this.schedulerConfigQuery.get(input.schedulerConfigId());

        // Validate the cron duplication if the cron expression or zone ID has changed
        if (!settlementSchedulerData.cronExpression().equals(input.cronExpression())) {

            List<SchedulerConfigData> settlementSchedulerList = this.settlementSchedulerQuery.getSettlementSchedulers(
                    settlementModelData.settlementModelId());

            settlementSchedulerList.removeIf(schedulerConfigData -> schedulerConfigData.schedulerConfigId()
                                                                                       .equals(input.schedulerConfigId()));


            boolean isOverlap = this.schedulerEngine.isCronOverlap(settlementSchedulerList, input.cronExpression());

            if (isOverlap) {
                throw new SettlementException(SettlementErrors.SETTLEMENT_SCHEDULER_OVERLAP.format(
                        settlementModelData.name()));
            }
        }

        var output =
                this.modifySchedulerConfigCommand.execute(new ModifySchedulerConfigCommandHandler.Input(input.schedulerConfigId(),
                                                                                                        input.name(),
                                                                                                        "CloseSettlementWindowsScheduler",
                                                                                                        input.description(),
                                                                                                        input.cronExpression(),
                                                                                                        settlementModelData.zoneId(),
                                                                                                        input.active()));
        this.schedulerEngine.scheduleOrReschedule(output.schedulerConfigData());

        return new Output(true);
    }

}
