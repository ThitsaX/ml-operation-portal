package com.thitsaworks.operation_portal.core.hub_services.data;

import java.io.Serializable;

public record HubSettlementModelData(Integer settlementModelId,
                                     String name,
                                     boolean isActive,
                                     Integer settlementGranularityId,
                                     Integer settlementInterchangeId,
                                     Integer settlementDelayId,
                                     String currencyId,
                                     boolean requireLiquidityCheck,
                                     Integer ledgerAccountTypeId,
                                     boolean autoPositionReset,
                                     boolean adjustPosition,
                                     Integer settlementAccountTypeId) implements Serializable {}
