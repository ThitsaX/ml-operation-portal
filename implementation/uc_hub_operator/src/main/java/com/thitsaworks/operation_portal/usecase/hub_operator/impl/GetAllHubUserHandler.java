package com.thitsaworks.operation_portal.usecase.hub_operator.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.hubuser.data.HubUserData;
import com.thitsaworks.operation_portal.core.hubuser.query.HubUserQuery;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.usecase.HubOperatorAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.hub_operator.GetAllHubUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class GetAllHubUserHandler extends HubOperatorAuditableUseCase<GetAllHubUser.Input, GetAllHubUser.Output>
    implements GetAllHubUser {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllHubUserHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.SUPERUSER,
                                                                    UserRoleType.ADMIN,
                                                                    UserRoleType.REPORTING,
                                                                    UserRoleType.OPERATION);

    private final HubUserQuery hubUserQuery;

    public GetAllHubUserHandler(CreateInputAuditCommand createInputAuditCommand,
                                CreateOutputAuditCommand createOutputAuditCommand,
                                CreateExceptionAuditCommand createExceptionAuditCommand,
                                ObjectMapper objectMapper,
                                PrincipalCache principalCache,
                                HubUserQuery hubUserQuery) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache);

        this.hubUserQuery = hubUserQuery;
    }

    @Override
    public Output onExecute(Input input) throws DomainException {

        List<HubUserData> hubUserDataList = this.hubUserQuery.getHubUsers();

        List<Output.HubUserInfo> userInfoList = new ArrayList<>();

        for (HubUserData hubUserData : hubUserDataList) {

            userInfoList.add(new Output.HubUserInfo(hubUserData.hubUserId(),
                                                    hubUserData.name(),
                                                    hubUserData.email(),
                                                    hubUserData.firstName(),
                                                    hubUserData.lastName(),
                                                    hubUserData.jobTitle(),
                                                    hubUserData.createdDate()));
        }

        return new GetAllHubUser.Output(userInfoList);
    }

}
