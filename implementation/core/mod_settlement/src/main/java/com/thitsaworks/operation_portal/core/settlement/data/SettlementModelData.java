package com.thitsaworks.operation_portal.core.settlement.data;

import com.thitsaworks.operation_portal.component.common.identifier.SchedulerConfigId;
import com.thitsaworks.operation_portal.component.common.identifier.SettlementModelId;
import com.thitsaworks.operation_portal.core.settlement.model.SettlementModel;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

public record SettlementModelData(SettlementModelId settlementModelId,
                                  String name,
                                  String type,
                                  String currencyId,
                                  boolean isActive,
                                  boolean autoCloseWindow,
                                  boolean requireLiquidityCheck,
                                  boolean autoPositionReset,
                                  boolean adjustPosition,
                                  Set<SchedulerConfigId> schedulerConfigIds) implements Serializable {

    public SettlementModelData(SettlementModel settlementModel) {

        this(settlementModel.getSettlementModelId(),
             settlementModel.getName(),
             settlementModel.getType(),
             settlementModel.getCurrencyId(),
             settlementModel.isActive(),
             settlementModel.isAutoCloseWindow(),
             settlementModel.isRequireLiquidityCheck(),
             settlementModel.isAutoPositionReset(),
             settlementModel.isAdjustPosition(),
             settlementModel.getSettlementSchedulerConfigs().stream()
                            .map(schedulerConfig -> new SchedulerConfigId(schedulerConfig.getSettlementSchedulerConfigId()
                                                                                         .getSchedulerConfigId()))
                            .collect(Collectors.toSet()));
    }

}
