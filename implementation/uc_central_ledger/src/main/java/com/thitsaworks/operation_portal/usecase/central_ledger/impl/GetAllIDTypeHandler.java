package com.thitsaworks.operation_portal.usecase.central_ledger.impl;

import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.reporting.central_ledger.query.GetIDTypesQuery;
import com.thitsaworks.operation_portal.usecase.CentralLedgerUseCase;
import com.thitsaworks.operation_portal.usecase.central_ledger.GetAllIDType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.Set;

@Service
public class GetAllIDTypeHandler extends CentralLedgerUseCase<GetAllIDType.Input, GetAllIDType.Output>
    implements GetAllIDType {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllIDTypeHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = EnumSet.allOf(UserRoleType.class);

    private final GetIDTypesQuery getIDTypesQuery;

    public GetAllIDTypeHandler(PrincipalCache principalCache,
                               GetIDTypesQuery getIDTypesQuery) {

        super(PERMITTED_ROLES, principalCache);

        this.getIDTypesQuery = getIDTypesQuery;

    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        GetIDTypesQuery.Output output = this.getIDTypesQuery.execute(new GetIDTypesQuery.Input());

        return new GetAllIDType.Output(output.getIdTypeDataList());
    }

}
