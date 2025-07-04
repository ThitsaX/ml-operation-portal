package com.thitsaworks.operation_portal.usecase.central_ledger;

import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;
import com.thitsaworks.operation_portal.reporting.central_ledger.data.IDTypeData;

import java.util.List;

public interface GetAllIDType extends
                              UseCase<GetAllIDType.Input, GetAllIDType.Output> {

    record Input() { }

    record Output(List<IDTypeData> idTypeDataList) { }

}
