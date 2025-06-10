package com.thitsaworks.operation_portal.component.usecase;

import com.thitsaworks.operation_portal.component.exception.DFSPPortalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

public abstract class AbstractPrivateUseCase<I, O> extends AbstractPublicUseCase<I, O> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractPrivateUseCase.class);

    @Override
    public O execute(I input) throws DFSPPortalException {

        LOG.info("Check authorization for : id :[{}], name :[{}]", this.getId(), this.getName());

        if (!this.isAuthorized(UseCaseContext.get())) {

            LOG.info("User is NOT authorized for : id :[{}], name :[{}]", this.getId(), this.getName());

            throw new UnauthorizedActionException(this.getName());
        }

        return super.execute(input);
    }

    public abstract boolean isAuthorized(Object userDetails);

    public static class UnauthorizedActionException extends DFSPPortalException {

        protected UnauthorizedActionException(String params) {

            super(params);
        }

        @Override
        public String errorCode() {

            return "UNAUTHORIZED_ACTION";
        }

        @Override
        public String defaultErrorMessage() {

            return MessageFormat.format("You are not allowed to perform this action {0}.", this.params);
        }

        @Override
        public boolean requireTranslation() {

            return true;
        }

    }

}
