package com.thitsaworks.operation_portal.usecase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.common.identifier.AuditId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.common.type.iamtesttype.ActionCode;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.exception.ErrorMessage;
import com.thitsaworks.operation_portal.component.misc.exception.SystemException;
import com.thitsaworks.operation_portal.component.misc.exception.UnauthorizedActionException;
import com.thitsaworks.operation_portal.component.misc.security.SecurityContext;
import com.thitsaworks.operation_portal.component.misc.usecase.DomainUseCase;
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
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public abstract class OperationPortalAuditableUseCase<I, O> extends DomainUseCase<I, O> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OperationPortalAuditableUseCase.class);

    private static final ThreadLocal<AuditId> auditId = new InheritableThreadLocal<>();

    private final Set<UserRoleType> PERMITTED_ROLES;

    private final ObjectMapper objectMapper;

    private final CreateInputAuditCommand createInputAuditCommand;

    private final CreateOutputAuditCommand createOutputAuditCommand;

    private final CreateExceptionAuditCommand createExceptionAuditCommand;

    private final PrincipalCache principalCache;

    private final ActionAuthorizationManager actionAuthorizationManager;

    public OperationPortalAuditableUseCase(CreateInputAuditCommand createInputAuditCommand,
                                           CreateOutputAuditCommand createOutputAuditCommand,
                                           CreateExceptionAuditCommand createExceptionAuditCommand,
                                           Set<UserRoleType> permittedRoles,
                                           ObjectMapper objectMapper,
                                           PrincipalCache principalCache,
                                           ActionAuthorizationManager actionAuthorizationManager) {

        this.createInputAuditCommand = createInputAuditCommand;
        this.createOutputAuditCommand = createOutputAuditCommand;
        this.createExceptionAuditCommand = createExceptionAuditCommand;
        this.PERMITTED_ROLES = permittedRoles;
        this.objectMapper = objectMapper;
        this.principalCache = principalCache;
        this.actionAuthorizationManager = actionAuthorizationManager;

    }

    @Override
    public String getName() {

        return this.getClass()
                   .getSimpleName()
                   .replaceFirst("Handler", "");
    }

    @PostConstruct
    @Override
    public void onConstruct() throws SystemException {

//        try {
//            String actionName = this.getName();
//            String scope = "OPERATION_PORTAL";
//            String description = "Auto-registered action for use case: " + actionName;
//
//            this.actionAuthorizationManager.registerAction(actionName, scope, description);
//
//        } catch (Exception e) {
//            LOGGER.error("Failed to register action [{}]: {}", getName(), e.getMessage());
//            throw new SystemException(new ErrorMessage("ACTION_REGISTRATION_FAILED", e.getMessage()));
//        }
    }

    @Override
    protected void afterExecute(O output) throws DomainException {

        var auditId = OperationPortalAuditableUseCase.auditId.get();

        /*
         * Audit Logging:
         * Logs input parameters and output results for auditing and traceability.
         */
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

    @Override
    protected void beforeExecute(I input) throws DomainException {

        SecurityContext securityContext = (SecurityContext) UseCaseContext.get();

        PrincipalData principalData =
            this.principalCache.get(new AccessKey(securityContext.accessKey()));

//        var userRole = principalData.userRoleType();
//        if (!PERMITTED_ROLES.contains(userRole)) {
//
//            LOGGER.info("User is NOT authorized for name :[{}]", this.getName());
//            throw new UnauthorizedActionException(IAMErrors.PERMISSION_DENIED);
//        }

        if(!this.actionAuthorizationManager.isAuthorizedTo(new UserId(principalData.principalId().getEntityId()), new ActionCode(this.getName()))){

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

        OperationPortalAuditableUseCase.auditId.set(this.createInputAuditCommand.execute(new CreateInputAuditCommand.Input(
                                                            this.getName(),
                                                            new UserId(
                                                                principalId.getId()),
                                                            principalData.realmId(),
                                                            inputInfo))
                                                                                .auditId());
    }

    @Override
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
                LOGGER.info("Audit Exception: [{}]",
                            e.getErrorMessage()
                             .description());
            }
        }

        if (exception instanceof DomainException) {
            return (DomainException) exception;
        }

        throw new RuntimeException(exception);
    }

}
