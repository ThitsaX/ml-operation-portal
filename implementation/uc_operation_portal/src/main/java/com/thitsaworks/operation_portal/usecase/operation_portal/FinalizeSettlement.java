package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;
import com.thitsaworks.operation_portal.core.hub_services.support.SettlementParticipant;
import com.thitsaworks.operation_portal.core.hub_services.support.SettlementWindow;

import java.io.Serializable;
import java.util.List;

public interface FinalizeSettlement
        extends UseCase<FinalizeSettlement.Input, FinalizeSettlement.Output> {

    public record Input(Integer settlementId){}

    public record Output(Boolean finalized){}

}
