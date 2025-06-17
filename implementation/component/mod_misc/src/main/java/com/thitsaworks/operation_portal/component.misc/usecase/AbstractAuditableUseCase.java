package com.thitsaworks.operation_portal.component.misc.usecase;

import com.thitsaworks.operation_portal.component.misc.exception.DFSPPortalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractAuditableUseCase<I, O> extends AbstractOwnableUseCase<I, O>{

    private static final Logger LOG = LoggerFactory.getLogger(AbstractPrivateUseCase.class);

    @Override
    public O execute(I input) throws DFSPPortalException {

        LOG.info("Check authorization for : id :[{}], name :[{}]", this.getId(), this.getName());

        O output = super.execute(input);

//        this.onAudit(input, output);

        return output;
    }

    public abstract void onAudit(I input, O output) throws DFSPPortalException;
}
