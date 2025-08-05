package com.thitsaworks.operation_portal.usecase;

import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.common.type.ActionCode;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.exception.UnauthorizedActionException;
import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
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
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.net.ConnectException;

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
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);

        this.beforeExecute(input);

        try {

            output = this.onExecute(input);

            transactionManager.commit(status);

            this.afterExecute(output);

        } catch (RuntimeException exception) {

            transactionManager.rollback(status);

            throw exception;

        } catch (Exception exception) {

            transactionManager.rollback(status);

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
            throw new UnauthorizedActionException(IAMErrors.PERMISSION_DENIED);
        }

    }

    protected void afterExecute(O output) throws DomainException { }

    protected DomainException onException(Exception exception) {

        if (exception instanceof DomainException) {
            return (DomainException) exception;
        }

        throw new RuntimeException(exception);
    }

    protected abstract O onExecute(I input) throws DomainException, ConnectException;

}
