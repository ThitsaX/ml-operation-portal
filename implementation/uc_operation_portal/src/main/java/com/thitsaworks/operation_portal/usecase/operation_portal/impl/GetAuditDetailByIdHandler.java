package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.query.GetAuditDetailByIdQuery;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetAuditDetailById;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.ConnectException;

@Service
public class GetAuditDetailByIdHandler
    extends OperationPortalUseCase<GetAuditDetailById.Input, GetAuditDetailById.Output>
    implements GetAuditDetailById {

    private static final Logger LOG = LoggerFactory.getLogger(GetAuditDetailByIdHandler.class);

    private final GetAuditDetailByIdQuery getAuditDetailByIdQuery;

    public GetAuditDetailByIdHandler(PrincipalCache principalCache,
                                     ActionAuthorizationManager actionAuthorizationManager,
                                     GetAuditDetailByIdQuery getAuditDetailByIdQuery) {

        super(principalCache, actionAuthorizationManager);

        this.getAuditDetailByIdQuery = getAuditDetailByIdQuery;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException, ConnectException {

        var
            output =
            this.getAuditDetailByIdQuery.execute(new GetAuditDetailByIdQuery.Input(input.auditId()))
                                        .auditData();

        return new Output(output.auditId(),
                          output.inputInfo(),
                          output.outputInfo(),
                          output.exceptionInfo());

    }

}
