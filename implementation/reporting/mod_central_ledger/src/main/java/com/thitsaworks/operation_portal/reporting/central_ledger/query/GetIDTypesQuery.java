package com.thitsaworks.operation_portal.reporting.central_ledger.query;

import com.thitsaworks.operation_portal.reporting.central_ledger.data.IDTypeData;
import com.thitsaworks.operation_portal.reporting.central_ledger.exception.CentralLedgerException;
import lombok.Value;

import java.util.List;

public interface GetIDTypesQuery {

    @Value
    class Input { }

    @Value
    class Output {

        private List<IDTypeData> idTypeDataList;

    }

    Output execute(Input input) throws CentralLedgerException;

}
