package com.thitsaworks.operation_portal.core.hub_services.query;


import com.thitsaworks.operation_portal.core.hub_services.data.IDTypeData;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesException;
import lombok.Value;

import java.util.List;

public interface GetIDTypesQuery {

    @Value
    class Input { }

    @Value
    class Output {

        private List<IDTypeData> idTypeDataList;

    }

    Output execute(Input input) throws HubServicesException;

}
