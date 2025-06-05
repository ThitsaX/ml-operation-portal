package com.thitsaworks.dfsp_portal.component.usecase;

import com.thitsaworks.dfsp_portal.component.exception.DFSPPortalException;
import com.thitsaworks.dfsp_portal.component.exception.SystemProblemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractPublicUseCase<I, O> {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractPublicUseCase.class);

    public O execute(I input) throws DFSPPortalException {

        LOG.info("Invoking UseCase : id : [{}], name :[{}]", this.getId(), this.getName());

        O output = null;

        try {

            output = this.onExecute(input);

        } catch (DFSPPortalException e) {

            LOG.error("(DFSPPortalException) Error : code: [{}], message :[{}]", e.errorCode(), e.defaultErrorMessage());
            LOG.error("Error details :", e);

            throw e;

        } catch (Exception e) {

            LOG.error("Error details :", e);

            throw new SystemProblemException(e.getMessage());
        }

        return output;
    }

    protected abstract O onExecute(I input) throws Exception;

    protected abstract String getName();

    protected abstract String getDescription();

    protected abstract String getScope();

    protected abstract String getId();

}