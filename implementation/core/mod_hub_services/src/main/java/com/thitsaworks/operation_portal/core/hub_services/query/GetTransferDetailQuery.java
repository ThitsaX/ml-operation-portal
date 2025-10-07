package com.thitsaworks.operation_portal.core.hub_services.query;

import com.thitsaworks.operation_portal.core.hub_services.data.TransferDetailData;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesException;

public interface GetTransferDetailQuery {

    TransferDetailData execute(String transferId, String timeZone) throws HubServicesException;

}
