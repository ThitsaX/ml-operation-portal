package com.thitsaworks.operation_portal.usecase.util;

import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.common.identifier.RoleId;
import com.thitsaworks.operation_portal.component.misc.security.SecurityContext;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCaseContext;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.data.PrincipalData;
import com.thitsaworks.operation_portal.core.iam.data.RoleData;
import com.thitsaworks.operation_portal.core.iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.iam.query.IAMQuery;
import com.thitsaworks.operation_portal.core.iam.query.RoleQuery;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserPermissionManager {

    private static final Logger LOG = LoggerFactory.getLogger(UserPermissionManager.class);

    private final IAMQuery iamQuery;

    private final RoleQuery roleQuery;

    private final PrincipalCache principalCache;

    public boolean isDfsp(PrincipalId principalId) throws IAMException {

        var roleList = this.iamQuery.getRoleListByPrincipal(principalId);

        var isDfsp = true;

        for (var role : roleList) {

            if (!role.isDfsp()) {
                isDfsp = false;
                break;
            }
        }

        return isDfsp;

    }

    public boolean areRolesAllowed(boolean isDfsp, List<RoleId> roleIdList)
        throws IAMException {

        List<RoleData> roleList = this.roleQuery.getAll();

        if (isDfsp) {
            roleList = roleList.stream()
                               .filter(role -> role.name() != null && role.name()
                                                                          .startsWith("DFSP"))
                               .toList();
        }

        Set<String> allowedRoleIds = roleList.stream()
                                             .map(r -> r.roleId()
                                                        .getId()
                                                        .toString())
                                             .collect(Collectors.toSet());

        return roleIdList != null
                   && !roleIdList.isEmpty()
                   && roleIdList.stream()
                                .allMatch(roleId -> roleId != null && allowedRoleIds.contains(roleId.getId()
                                                                                                    .toString()));

    }

    public PrincipalData getCurrentUser() throws IAMException {

        SecurityContext securityContext = (SecurityContext) UseCaseContext.get();

        PrincipalData currentUser =
            this.principalCache.get(new AccessKey(securityContext.accessKey()));

        if (currentUser == null) {
            throw new IAMException(IAMErrors.PRINCIPAL_NOT_FOUND.format(securityContext.userId()
                                                                                       .toString()));

        }

        return currentUser;
    }

    public boolean isSameParticipant(ParticipantId loggedInUserParticipantId, ParticipantId requestParticipantId) {

        return loggedInUserParticipantId.equals(requestParticipantId);
    }

}
