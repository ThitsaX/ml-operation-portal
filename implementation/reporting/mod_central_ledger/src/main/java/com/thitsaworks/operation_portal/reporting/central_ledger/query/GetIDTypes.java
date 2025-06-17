package com.thitsaworks.operation_portal.reporting.central_ledger.query;

import com.thitsaworks.operation_portal.reporting.central_ledger.data.IDTypeData;
import lombok.Value;

import java.util.List;

public interface GetIDTypes {

    @Value
    class Input {

    }

    @Value
    class Output {

        private List<IDTypeData> idTypeDataList;

    }

    Output execute(Input input) throws Exception;

}
