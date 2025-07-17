package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;
import com.thitsaworks.operation_portal.core.hub_services.data.IDTypeData;

import java.util.List;

public interface GetAllIDType extends
                              UseCase<GetAllIDType.Input, GetAllIDType.Output> {

    record Input() { }

    record Output(List<IDTypeData> idTypeDataList) { }

}
