package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.data.RoleData;
import com.thitsaworks.operation_portal.core.iam.query.IAMQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetRoleList;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.ConnectException;
import java.util.List;

@Service
public class GetRoleListHandler
    extends OperationPortalUseCase<GetRoleList.Input, GetRoleList.Output> implements GetRoleList {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetRoleListHandler.class);

    private final IAMQuery iamQuery;

    public GetRoleListHandler(PrincipalCache principalCache,
                              ActionAuthorizationManager actionAuthorizationManager,
                              IAMQuery iamQuery) {

        super(principalCache, actionAuthorizationManager);

        this.iamQuery = iamQuery;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException, ConnectException {

        List<RoleData> roleList = this.iamQuery.getRoleList();

        return new Output(roleList);
    }

}
