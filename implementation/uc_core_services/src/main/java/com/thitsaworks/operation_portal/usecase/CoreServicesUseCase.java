package com.thitsaworks.operation_portal.usecase;

import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.exception.SystemException;
import com.thitsaworks.operation_portal.component.misc.exception.UnauthorizedActionException;
import com.thitsaworks.operation_portal.component.misc.security.SecurityContext;
import com.thitsaworks.operation_portal.component.misc.usecase.DomainUseCase;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCaseContext;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.data.PrincipalData;
import com.thitsaworks.operation_portal.core.iam.exception.IAMErrors;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public abstract class CoreServicesUseCase<I, O> extends DomainUseCase<I, O> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CoreServicesUseCase.class);

    private final Set<UserRoleType> PERMITTED_ROLES;

    private final PrincipalCache principalCache;

    public CoreServicesUseCase(Set<UserRoleType> permittedRoles,
                               PrincipalCache principalCache) {

        this.PERMITTED_ROLES = permittedRoles;
        this.principalCache = principalCache;
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

        // Do nothing...
    }

    @Override
    protected void afterExecute(O output) throws DomainException {

        // PLease Do Something

    }

    @Override
    protected void beforeExecute(I input) throws DomainException {

        SecurityContext securityContext = (SecurityContext) UseCaseContext.get();

        PrincipalData principalData =
            this.principalCache.get(new AccessKey(securityContext.accessKey()));

        /*
         * Authorization Check:
         * Ensures the current user has the required role(s) (e.g., ADMIN, OPERATION)
         * to access or perform this operation. Denies access if insufficient privileges.
         */
        var userRole = principalData.userRoleType();

        if (!PERMITTED_ROLES.contains(userRole)) {

            LOGGER.info("User is NOT authorized for name :[{}]", this.getName());
            throw new UnauthorizedActionException(IAMErrors.PERMISSION_DENIED);
        }

    }

    @Override
    protected DomainException onException(Exception exception) {

        if (exception instanceof DomainException) {
            return (DomainException) exception;
        }

        throw new RuntimeException(exception);
    }

}
