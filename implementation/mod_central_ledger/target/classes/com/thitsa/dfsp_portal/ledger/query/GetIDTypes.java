package com.thitsa.dfsp_portal.ledger.query;

import com.thitsa.dfsp_portal.ledger.data.IDTypeData;
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
