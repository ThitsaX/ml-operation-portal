package com.thitsaworks.operation_portal.core.settlement.query;

import com.thitsaworks.operation_portal.component.common.identifier.SettlementModelId;
import com.thitsaworks.operation_portal.core.scheduler.data.SchedulerConfigData;

import java.util.List;

public interface SettlementSchedulerQuery {

    List<SchedulerConfigData> getSettlementSchedulers(SettlementModelId settlementModelId);

}

