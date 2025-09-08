package com.thitsaworks.operation_portal.usecase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.common.identifier.AuditId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.type.ActionCode;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.exception.ErrorMessage;
import com.thitsaworks.operation_portal.component.misc.exception.SystemException;
import com.thitsaworks.operation_portal.component.misc.exception.UnauthorizedActionException;
import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.thitsaworks.operation_portal.component.misc.security.SecurityContext;
import com.thitsaworks.operation_portal.component.misc.spring.SpringContext;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCaseContext;
import com.thitsaworks.operation_portal.component.misc.util.MaskPassword;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.exception.AuditException;
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

public abstract class OperationPortalAuditableUseCase<I, O> implements UseCase<I, O> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OperationPortalAuditableUseCase.class);

    private static final ThreadLocal<AuditId> auditId = new InheritableThreadLocal<>();

    private final ObjectMapper objectMapper;

    private final CreateInputAuditCommand createInputAuditCommand;

    private final CreateOutputAuditCommand createOutputAuditCommand;

    private final CreateExceptionAuditCommand createExceptionAuditCommand;

    private final PrincipalCache principalCache;

    private final ActionAuthorizationManager actionAuthorizationManager;

    public OperationPortalAuditableUseCase(CreateInputAuditCommand createInputAuditCommand,
                                           CreateOutputAuditCommand createOutputAuditCommand,
                                           CreateExceptionAuditCommand createExceptionAuditCommand,
                                           ObjectMapper objectMapper,
                                           PrincipalCache principalCache,
                                           ActionAuthorizationManager actionAuthorizationManager) {

        this.createInputAuditCommand = createInputAuditCommand;
        this.createOutputAuditCommand = createOutputAuditCommand;
        this.createExceptionAuditCommand = createExceptionAuditCommand;
        this.objectMapper = objectMapper;
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

        String inputJson, inputInfo;
        try {

            inputJson = this.objectMapper.writeValueAsString(input);
            inputInfo = MaskPassword.maskPassword(this.objectMapper, inputJson);

        } catch (Exception e) {
            throw new SystemException(new ErrorMessage(e.getMessage(), e.getMessage()));
        }

        var principalId = principalData.principalId();

        var action = this.actionAuthorizationManager.getAction(new ActionCode(this.getName()));

        OperationPortalAuditableUseCase.auditId.set(this.createInputAuditCommand.execute(new CreateInputAuditCommand.Input(
                                                            action.actionId(),
                                                            new UserId(
                                                                principalId.getId()),
                                                            principalData.realmId(),
                                                            inputInfo))
                                                                                .auditId());
    }

    protected void afterExecute(O output) throws DomainException {

        var auditId = OperationPortalAuditableUseCase.auditId.get();

        String outputJson, outputInfo;

        try {

            outputJson = this.objectMapper.writeValueAsString(output);
            outputInfo = MaskPassword.maskPassword(this.objectMapper, outputJson);

        } catch (Exception e) {

            throw new SystemException(new ErrorMessage(e.getMessage(), e.getMessage()));
        }

        if (auditId != null) {

            this.createOutputAuditCommand.execute(new CreateOutputAuditCommand.Input(auditId,
                                                                                     outputInfo));

        }

    }

    protected DomainException onException(Exception exception) {

        String exceptionMessage = (exception instanceof DomainException e)
                                      ? e.getErrorMessage()
                                         .code() + " - " + e.getErrorMessage()
                                                            .description()
                                      : exception.getMessage();

        var auditId = OperationPortalAuditableUseCase.auditId.get();

        if (auditId != null) {

            try {

                var input = new CreateExceptionAuditCommand.Input(auditId, exceptionMessage);
                this.createExceptionAuditCommand.execute(input);

            } catch (AuditException e) {

                LOGGER.info("Audit Exception: [{}]", e.getErrorMessage()
                                                      .description());
            }
        }

        if (exception instanceof DomainException) {
            return (DomainException) exception;
        }

        throw new RuntimeException(exception);
    }

    protected abstract O onExecute(I input) throws DomainException, ConnectException;

}
