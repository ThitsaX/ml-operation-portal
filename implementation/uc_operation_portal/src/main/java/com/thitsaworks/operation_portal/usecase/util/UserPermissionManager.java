package com.thitsaworks.operation_portal.usecase.util;

import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.iam.query.IAMQuery;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPermissionManager {

    private static final Logger LOG = LoggerFactory.getLogger(UserPermissionManager.class);

    private final IAMQuery iamQuery;

    public boolean isDfsp(PrincipalId principalId) throws IAMException {

        var roleList = this.iamQuery.getRolesByPrincipal(principalId);

        var isDfsp = true;

        for (var role : roleList) {

            if (!role.isDfsp()) {
                isDfsp = false;
                break;
            }
        }

        return isDfsp;

    }

}
