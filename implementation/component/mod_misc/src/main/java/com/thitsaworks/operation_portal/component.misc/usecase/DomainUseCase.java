package com.thitsaworks.operation_portal.component.misc.usecase;

import com.thitsaworks.operation_portal.component.misc.exception.OperationPortalException;
import com.thitsaworks.operation_portal.component.misc.exception.SystemException;

public abstract class DomainUseCase<I, O> implements UseCase<I, O> {

    public O execute(I input) throws OperationPortalException {

        O output;

        this.beforeExecute(input);

        try {

            output = this.onExecute(input);

            this.afterExecute(output);

        } catch (RuntimeException exception) {

            throw exception;

        } catch (Exception exception) {

            throw this.onException(exception);
        }

        return output;
    }

    public abstract String getName();

    public abstract void onConstruct() throws SystemException;

    protected abstract void afterExecute(O output) throws OperationPortalException;

    protected abstract void beforeExecute(I input) throws OperationPortalException;

    protected abstract OperationPortalException onException(Exception exception);

    protected abstract O onExecute(I input) throws OperationPortalException;

}
