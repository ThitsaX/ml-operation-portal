package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.data.UserData;
import com.thitsaworks.operation_portal.core.participant.query.UserQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetParticipantUserListByParticipant;
import com.thitsaworks.operation_portal.usecase.util.UserPermissionManager;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.springframework.stereotype.Service;

import java.net.ConnectException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class GetParticipantUserListByParticipantHandler
    extends OperationPortalUseCase<GetParticipantUserListByParticipant.Input, GetParticipantUserListByParticipant.Output>
    implements GetParticipantUserListByParticipant {

    private final UserQuery userQuery;

    private final PrincipalCache principalCache;

    private final UserPermissionManager userPermissionManager;

    public GetParticipantUserListByParticipantHandler(PrincipalCache principalCache,
                                                      ActionAuthorizationManager actionAuthorizationManager,
                                                      UserQuery userQuery,
                                                      UserPermissionManager userPermissionManager) {

        super(principalCache,
              actionAuthorizationManager);

        this.userQuery = userQuery;
        this.principalCache = principalCache;
        this.userPermissionManager = userPermissionManager;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException, ConnectException {

        var currentUser = this.userPermissionManager.getCurrentUser();

        Set<Output.User> madeByUsers = new HashSet<>();

        List<UserData> userDataList;

        if (this.userPermissionManager.isDfsp(currentUser.principalId())) {

            userDataList = this.userQuery.getUsers(new ParticipantId(currentUser.realmId()
                                                                                .getId()));

            for (var userData : userDataList) {

                madeByUsers.add(new Output.User(new UserId(userData.userId()
                                                                   .getEntityId()), userData.email()));

            }

        } else {

            userDataList = this.userQuery.getUsers();

            for (var userData : userDataList) {

                madeByUsers.add(new Output.User(new UserId(userData.userId()
                                                                   .getEntityId()), userData.email()));
            }

        }

        return new Output(madeByUsers);

    }

}