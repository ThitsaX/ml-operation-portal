package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public interface FinalizeSettlement
    extends UseCase<FinalizeSettlement.Input, FinalizeSettlement.Output> {

    record Input(Integer settlementId) { }

    record Output(Boolean finalized){}

}
