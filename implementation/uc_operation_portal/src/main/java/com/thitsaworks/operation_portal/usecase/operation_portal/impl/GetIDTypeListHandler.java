package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.hub_services.query.GetIDTypesQuery;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;

import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetIDTypeList;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.Set;

@Service
public class GetIDTypeListHandler extends OperationPortalUseCase<GetIDTypeList.Input, GetIDTypeList.Output>
    implements GetIDTypeList {

    private static final Logger LOG = LoggerFactory.getLogger(GetIDTypeListHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = EnumSet.allOf(UserRoleType.class);

    private final GetIDTypesQuery getIDTypesQuery;

    public GetIDTypeListHandler(PrincipalCache principalCache,
                                ActionAuthorizationManager actionAuthorizationManager,
                                GetIDTypesQuery getIDTypesQuery) {

        super(PERMITTED_ROLES,
              principalCache,
              actionAuthorizationManager);

        this.getIDTypesQuery = getIDTypesQuery;

    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        GetIDTypesQuery.Output output = this.getIDTypesQuery.execute(new GetIDTypesQuery.Input());

        return new GetIDTypeList.Output(output.getIdTypeDataList());
    }

}
