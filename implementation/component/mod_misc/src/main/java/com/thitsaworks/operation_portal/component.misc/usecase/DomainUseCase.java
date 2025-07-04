package com.thitsaworks.operation_portal.component.misc.usecase;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.exception.SystemException;

public abstract class DomainUseCase<I, O> implements UseCase<I, O> {

    public O execute(I input) throws DomainException {

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

    protected abstract void afterExecute(O output) throws DomainException;

    protected abstract void beforeExecute(I input) throws DomainException;

    protected abstract DomainException onException(Exception exception);

    protected abstract O onExecute(I input) throws DomainException;

}
