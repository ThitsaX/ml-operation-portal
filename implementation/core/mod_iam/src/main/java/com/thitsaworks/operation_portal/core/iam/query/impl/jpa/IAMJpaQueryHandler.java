package com.thitsaworks.operation_portal.core.iam.query.impl.jpa;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.thitsaworks.operation_portal.core.iam.data.ActionData;
import com.thitsaworks.operation_portal.core.iam.data.PrincipalData;
import com.thitsaworks.operation_portal.core.iam.data.RoleData;
import com.thitsaworks.operation_portal.core.iam.engine.IAMEngine;
import com.thitsaworks.operation_portal.core.iam.model.Action;
import com.thitsaworks.operation_portal.core.iam.model.Principal;
import com.thitsaworks.operation_portal.core.iam.model.QAction;
import com.thitsaworks.operation_portal.core.iam.model.QPrincipal;
import com.thitsaworks.operation_portal.core.iam.model.QRole;
import com.thitsaworks.operation_portal.core.iam.model.QRoleGrant;
import com.thitsaworks.operation_portal.core.iam.model.Role;
import com.thitsaworks.operation_portal.core.iam.model.repository.ActionRepository;
import com.thitsaworks.operation_portal.core.iam.model.repository.PrincipalRepository;
import com.thitsaworks.operation_portal.core.iam.model.repository.RoleGrantRepository;
import com.thitsaworks.operation_portal.core.iam.model.repository.RoleRepository;
import com.thitsaworks.operation_portal.core.iam.query.IAMQuery;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IAMJpaQueryHandler implements IAMQuery {

    private static final Logger LOG = LoggerFactory.getLogger(IAMJpaQueryHandler.class);

    private final QAction iamAction = QAction.action;

    private final QRole role = QRole.role;

    private final QPrincipal principal = QPrincipal.principal;

    private final QRoleGrant roleGrant = QRoleGrant.roleGrant;

    private final ActionRepository actionRepository;

    private final RoleRepository roleRepository;

    private final PrincipalRepository principalRepository;

    private final RoleGrantRepository roleGrantRepository;

    private final IAMEngine iamEngine;

    @Override
    public List<RoleData> getRoles() {

        BooleanExpression predicate = this.role.isNotNull();

        var roles = (List<Role>) this.roleRepository.findAll(predicate);

        return roles.stream()
                    .map(RoleData::new)
                    .toList();
    }

    public List<PrincipalData> getPrincipals() {

        BooleanExpression predicate = this.principal.isNotNull();

        var principals = (List<Principal>) this.principalRepository.findAll(predicate);

        return principals.stream()
                         .map(PrincipalData::new)
                         .toList();
    }

    @Override
    public List<ActionData> getActions() {

        List<ActionData> availableActions = this.iamEngine.getActions();

        if (availableActions != null) {
            return availableActions;
        }

        BooleanExpression filter = this.iamAction.isNotNull();
        List<Action> fetchedActions = (List<Action>) this.actionRepository.findAll(filter);

        if (fetchedActions.isEmpty()) {
            return Collections.emptyList();
        }

        List<ActionData> actionDataList = fetchedActions.stream()
                                                        .map(ActionData::new)
                                                        .toList();

        actionDataList.forEach(action -> this.iamEngine.addAction(action.actionId(),
                                                                  action.actionCode(),
                                                                  action));

        return fetchedActions.stream()
                             .map(ActionData::new)
                             .toList();

    }

}
