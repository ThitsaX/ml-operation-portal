package com.thitsaworks.operation_portal.usecase.central_ledger;

import com.thitsaworks.operation_portal.component.misc.usecase.AbstractOwnableUseCase;
import com.thitsaworks.operation_portal.reporting.central_ledger.data.IDTypeData;
import java.util.List;

public abstract class GetAllIDType extends
        AbstractOwnableUseCase<GetAllIDType.Input, GetAllIDType.Output> {

    public record Input() {}

    public record Output(List<IDTypeData> idTypeDataList) {}
}
