package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.scheduler.command.ModifySchedulerConfigCommand;
import com.thitsaworks.operation_portal.core.scheduler.data.SchedulerConfigData;
import com.thitsaworks.operation_portal.core.settlement.command.ModifySettlementModelCommand;
import com.thitsaworks.operation_portal.core.settlement.command.impl.ModifySettlementModelCommandHandler;
import com.thitsaworks.operation_portal.core.settlement.data.SettlementModelData;
import com.thitsaworks.operation_portal.core.settlement.exception.SettlementErrors;
import com.thitsaworks.operation_portal.core.settlement.exception.SettlementException;
import com.thitsaworks.operation_portal.core.settlement.query.SettlementModelQuery;
import com.thitsaworks.operation_portal.core.settlement.query.SettlementSchedulerQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.ModifySettlementModel;
import com.thitsaworks.operation_portal.usecase.operation_portal.scheduler.SchedulerEngine;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ModifySettlementModelHandler
    extends OperationPortalAuditableUseCase<ModifySettlementModel.Input, ModifySettlementModel.Output>
    implements ModifySettlementModel {

    private static final Logger LOG = LoggerFactory.getLogger(ModifySettlementModelHandler.class);

    private final SettlementModelQuery settlementModelQuery;

    private final SettlementSchedulerQuery settlementSchedulerQuery;

    private final ModifySettlementModelCommand modifySettlementModelCommand;

    private final ModifySchedulerConfigCommand modifySchedulerConfigCommand;

    private final SchedulerEngine schedulerEngine;

    private final ObjectMapper objectMapper;

    public ModifySettlementModelHandler(CreateInputAuditCommand createInputAuditCommand,
                                        CreateOutputAuditCommand createOutputAuditCommand,
                                        CreateExceptionAuditCommand createExceptionAuditCommand,
                                        ObjectMapper objectMapper,
                                        PrincipalCache principalCache,
                                        ActionAuthorizationManager actionAuthorizationManager,
                                        SettlementModelQuery settlementModelQuery,
                                        SettlementSchedulerQuery settlementSchedulerQuery,
                                        ModifySettlementModelCommand modifySettlementModelCommand,
                                        ModifySchedulerConfigCommand modifySchedulerConfigCommand,
                                        SchedulerEngine schedulerEngine) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.settlementModelQuery = settlementModelQuery;
        this.settlementSchedulerQuery = settlementSchedulerQuery;
        this.modifySettlementModelCommand = modifySettlementModelCommand;
        this.modifySchedulerConfigCommand = modifySchedulerConfigCommand;
        this.schedulerEngine = schedulerEngine;
        this.objectMapper = objectMapper;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        SettlementModelData settlementModelData = this.settlementModelQuery.get(input.settlementModelId());

        if (!input.autoCloseWindow() && !input.manualCloseWindow()) {
            throw new SettlementException(SettlementErrors.WINDOW_CLOSING_METHOD_MISSING.format(
                settlementModelData.name()));
        }

        List<SchedulerConfigData> settlementSchedulers =
                this.settlementSchedulerQuery.getSettlementSchedulers(input.settlementModelId());

        boolean zoneChanged = true;
        if (!settlementModelData.zoneId().isEmpty()) {
            zoneChanged = !ZoneOffset.from(ZonedDateTime.now(ZoneId.of(settlementModelData.zoneId())))
                                             .equals(ZoneOffset.from(ZonedDateTime.now(ZoneId.of(input.zoneId()))));
        }

        boolean statusChanged = input.isActive() != settlementModelData.isActive();

        boolean methodChanged = input.autoCloseWindow() != settlementModelData.autoCloseWindow();

        if (zoneChanged || methodChanged || statusChanged) {

            if (zoneChanged) {

                List<SchedulerConfigData> updatedSchedulerList = new ArrayList<>();

                for (SchedulerConfigData scheduler : settlementSchedulers) {
                    scheduler =
                            this.modifySchedulerConfigCommand.execute(new ModifySchedulerConfigCommand.Input(
                                    scheduler.schedulerConfigId(),
                                    scheduler.name(),
                                    scheduler.jobName(),
                                    scheduler.description(),
                                    scheduler.cronExpression(),
                                    input.zoneId(),
                                    scheduler.active())).schedulerConfigData();

                    updatedSchedulerList.add(scheduler);
                }

                settlementSchedulers = updatedSchedulerList;
            }

            boolean isSchedulerActive = input.isActive() && input.autoCloseWindow();

            this.schedulerEngine.rescheduleSettlementSchedulers(settlementSchedulers, isSchedulerActive);

        }

        var output = this.modifySettlementModelCommand.execute(new ModifySettlementModelCommandHandler.Input(
            settlementModelData.settlementModelId(),
            input.settlementModelName(),
            input.modelType(),
            input.currencyID(),
            input.isActive(),
            input.autoCloseWindow(),
            input.manualCloseWindow(),
            input.zoneId()));

        return new Output(output.modified(), output.settlementModelId());

    }

}
