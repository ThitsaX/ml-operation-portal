package com.thitsaworks.operation_portal.usecase;

import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.common.type.ActionCode;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.exception.UnauthorizedActionException;
import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.thitsaworks.operation_portal.component.misc.persistence.TransactionContext;
import com.thitsaworks.operation_portal.component.misc.security.SecurityContext;
import com.thitsaworks.operation_portal.component.misc.spring.SpringContext;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCaseContext;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.data.PrincipalData;
import com.thitsaworks.operation_portal.core.iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.PlatformTransactionManager;

import java.net.ConnectException;
import java.util.UUID;

public abstract class OperationPortalUseCase<I, O> implements UseCase<I, O> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OperationPortalUseCase.class);

    private final PrincipalCache principalCache;

    private final ActionAuthorizationManager actionAuthorizationManager;

    public OperationPortalUseCase(PrincipalCache principalCache,
                                  ActionAuthorizationManager actionAuthorizationManager) {

        this.principalCache = principalCache;
        this.actionAuthorizationManager = actionAuthorizationManager;
    }

    public String getName() {

        return this.getClass()
                   .getSimpleName()
                   .replaceFirst("Handler", "");
    }

    @Override
    public O execute(I input) throws DomainException {

        O output;

        var transactionManager = SpringContext.getBean(PlatformTransactionManager.class,
                                                       PersistenceQualifiers.Core.TRANSACTION_MANAGER);

        this.beforeExecute(input);

        try {

            TransactionContext.startNew(transactionManager, UUID.randomUUID()
                                                                .toString());

            output = this.onExecute(input);

            TransactionContext.commit();

            this.afterExecute(output);

        } catch (RuntimeException exception) {

            TransactionContext.rollback();

            throw exception;

        } catch (Exception exception) {

            TransactionContext.rollback();

            throw this.onException(exception);
        }

        return output;
    }

    protected void beforeExecute(I input) throws DomainException {

        SecurityContext securityContext = (SecurityContext) UseCaseContext.get();

        PrincipalData principalData =
            this.principalCache.get(new AccessKey(securityContext.accessKey()));

        if (!this.actionAuthorizationManager.isAuthorizedTo(principalData.principalId(),
                                                            new ActionCode(this.getName()))) {

            LOGGER.info("User is NOT authorized for name :[{}]", this.getName());
            throw new UnauthorizedActionException(IAMErrors.PERMISSION_DENIED.format(this.getName()));
        }

    }

    protected void afterExecute(O output) throws DomainException { }

    protected DomainException onException(Exception exception) {

        if (exception instanceof DomainException) {
            return (DomainException) exception;
        }

        LOGGER.info("Runtime Exception: [{}]", exception.getMessage());

        throw new RuntimeException(exception);
    }

    protected abstract O onExecute(I input) throws DomainException, ConnectException;

}
