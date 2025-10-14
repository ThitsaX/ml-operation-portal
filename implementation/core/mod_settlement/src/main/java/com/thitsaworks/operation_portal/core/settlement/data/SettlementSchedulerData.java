package com.thitsaworks.operation_portal.core.settlement.data;

import com.thitsaworks.operation_portal.component.common.identifier.SchedulerConfigId;
import com.thitsaworks.operation_portal.component.common.identifier.SettlementModelId;

import java.io.Serializable;

public record SettlementSchedulerData(SettlementModelId settlementModelId,
                                      SchedulerConfigId schedulerConfigId) implements Serializable {

}
