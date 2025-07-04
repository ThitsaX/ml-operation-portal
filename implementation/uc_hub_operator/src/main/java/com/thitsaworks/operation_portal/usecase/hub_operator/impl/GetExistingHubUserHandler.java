package com.thitsaworks.operation_portal.usecase.hub_operator.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.OperationPortalException;
import com.thitsaworks.operation_portal.component.misc.security.SecurityContext;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCaseContext;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.exception.UserNotFoundException;
import com.thitsaworks.operation_portal.core.audit.model.Auditor;
import com.thitsaworks.operation_portal.core.hubuser.data.HubUserData;
import com.thitsaworks.operation_portal.core.hubuser.query.HubUserQuery;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.usecase.HubOperatorAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.hub_operator.GetExistingHubUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class GetExistingHubUserHandler
        extends HubOperatorAuditableUseCase<GetExistingHubUser.Input, GetExistingHubUser.Output>
        implements GetExistingHubUser {

    private static final Logger LOG = LoggerFactory.getLogger(GetExistingHubUserHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.ADMIN,
                                                                    UserRoleType.OPERATION,
                                                                    UserRoleType.REPORTING,
                                                                    UserRoleType.SUPERUSER);

    private final HubUserQuery hubUserQuery;

    private final ObjectMapper objectMapper;

    @Autowired
    public GetExistingHubUserHandler(CreateInputAuditCommand createInputAuditCommand,
                                     CreateOutputAuditCommand createOutputAuditCommand,
                                     CreateExceptionAuditCommand createExceptionAuditCommand,
                                     HubUserQuery hubUserQuery,
                                     ObjectMapper objectMapper,
                                     PrincipalCache principalCache) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache);

        this.hubUserQuery = hubUserQuery;
        this.objectMapper = objectMapper;
    }

    @Override
    public GetExistingHubUser.Output onExecute(GetExistingHubUser.Input input) throws OperationPortalException {

        HubUserData hubUserData = this.hubUserQuery.get(input.hubUserId());

        return new GetExistingHubUser.Output(hubUserData.hubUserId(),
                                             hubUserData.name(),
                                             hubUserData.email(),
                                             hubUserData.firstName(),
                                             hubUserData.lastName(),
                                             hubUserData.jobTitle(),
                                             hubUserData.createdDate().getEpochSecond());
    }

//    @Override
//    protected String getName() {
//
//        return GetExistingHubUser.class.getCanonicalName();
//    }
//
//    @Override
//    protected String getDescription() {
//
//        return null;
//    }
//
//    @Override
//    protected String getScope() {
//
//        return "uc_hub_operator";
//    }
//
//    @Override
//    protected String getId() {
//
//        return GetExistingHubUser.class.getName();
//    }
//
//    @Override
//    public boolean isOwned(Object userDetails) {
//
//        return true;
//    }
//
//    @Override
//    public boolean isAuthorized(Object userDetails) {
//
//        return true;
//    }
//
//    @Override
//    public void onAudit(Input input, Output output) throws UserNotFoundException {
//
//        SecurityContext securityContext = (SecurityContext) UseCaseContext.get();
//
//        Auditor.audit(this.objectMapper, GetExistingHubUser.class, input, output,
//                      new UserId(securityContext.userId()),
//                      securityContext.realmId() == null ? null : new RealmId(securityContext.realmId()));
//    }

}
