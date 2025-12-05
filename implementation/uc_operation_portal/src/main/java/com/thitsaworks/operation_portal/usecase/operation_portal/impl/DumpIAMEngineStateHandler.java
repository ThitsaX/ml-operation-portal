package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.engine.IAMEngine;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.DumpIAMEngineState;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.ConnectException;

@Service
public class DumpIAMEngineStateHandler
    extends OperationPortalUseCase<DumpIAMEngineState.Input, DumpIAMEngineState.Output>
    implements DumpIAMEngineState {

    private static final Logger LOG = LoggerFactory.getLogger(DumpIAMEngineStateHandler.class);

    private final IAMEngine iamEngine;

    public DumpIAMEngineStateHandler(PrincipalCache principalCache,
                                     ActionAuthorizationManager actionAuthorizationManager,
                                     IAMEngine iamEngine) {

        super(principalCache,
              actionAuthorizationManager);
        this.iamEngine = iamEngine;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException, ConnectException {

        var engineState = this.iamEngine.dumpEngineState();

        return new Output(engineState);
    }

}
