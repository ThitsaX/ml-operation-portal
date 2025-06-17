package com.thitsaworks.operation_portal.usecase.central_ledger.impl;

import com.thitsaworks.operation_portal.reporting.central_ledger.query.GetIDTypes;
import com.thitsaworks.operation_portal.usecase.central_ledger.GetAllIDType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetAllIDTypeHandler extends GetAllIDType {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllIDTypeHandler.class);

    private GetIDTypes getIDTypes;

    @Autowired
    public GetAllIDTypeHandler(GetIDTypes getIDTypes) {

        this.getIDTypes = getIDTypes;
    }

    @Override
    public GetAllIDType.Output onExecute(GetAllIDType.Input input) throws Exception {

        GetIDTypes.Output output = this.getIDTypes.execute(new GetIDTypes.Input());

        return new GetAllIDType.Output(output.getIdTypeDataList());
    }

    @Override
    protected String getName() {

        return GetAllIDType.class.getCanonicalName();
    }

    @Override
    protected String getDescription() {

        return null;
    }

    @Override
    protected String getScope() {

        return "uc_central_ledger";
    }

    @Override
    protected String getId() {

        return GetAllIDType.class.getName();
    }

    @Override
    public boolean isOwned(Object userDetails) {

        return true;
    }

    @Override
    public boolean isAuthorized(Object userDetails) {

        return true;
    }

}
