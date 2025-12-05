package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.engine.IAMEngine;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.RefreshIAMEngine;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.ConnectException;

@Service
public class RefreshIAMEngineHandler extends OperationPortalUseCase<RefreshIAMEngine.Input, RefreshIAMEngine.Output>
    implements RefreshIAMEngine {

    private static final Logger LOG = LoggerFactory.getLogger(RefreshIAMEngineHandler.class);

    private final IAMEngine iamEngine;

    public RefreshIAMEngineHandler(PrincipalCache principalCache,
                                   ActionAuthorizationManager actionAuthorizationManager,
                                   IAMEngine iamEngine) {

        super(principalCache,
              actionAuthorizationManager);

        this.iamEngine = iamEngine;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException, ConnectException {

        this.iamEngine.bootstrap();

        LOG.info("IAM Engine refreshed successfully!");

        return new Output(true);
    }

}
