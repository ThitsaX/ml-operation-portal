package com.thitsaworks.operation_portal.usecase.central_ledger;

import com.thitsaworks.operation_portal.component.misc.usecase.AbstractOwnableUseCase;
import com.thitsaworks.operation_portal.reporting.central_ledger.data.IDTypeData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public abstract class GetAllIDType extends
                                   AbstractOwnableUseCase<GetAllIDType.Input, GetAllIDType.Output> {

    @Getter
    @NoArgsConstructor
    public static class Input {

    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Output {

        private List<IDTypeData> idTypeDataList;

    }

}
