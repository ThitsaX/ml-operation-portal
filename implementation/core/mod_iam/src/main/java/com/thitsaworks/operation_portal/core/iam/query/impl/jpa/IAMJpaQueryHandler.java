package com.thitsaworks.operation_portal.core.iam.query.impl.jpa;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.thitsaworks.operation_portal.core.iam.data.ActionData;
import com.thitsaworks.operation_portal.core.iam.data.PrincipalData;
import com.thitsaworks.operation_portal.core.iam.data.RoleData;
import com.thitsaworks.operation_portal.core.iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.iam.model.Action;
import com.thitsaworks.operation_portal.core.iam.model.Principal;
import com.thitsaworks.operation_portal.core.iam.model.QAction;
import com.thitsaworks.operation_portal.core.iam.model.QPrincipal;
import com.thitsaworks.operation_portal.core.iam.model.QRole;
import com.thitsaworks.operation_portal.core.iam.model.QRoleGrant;
import com.thitsaworks.operation_portal.core.iam.model.Role;
import com.thitsaworks.operation_portal.core.iam.model.RoleGrant;
import com.thitsaworks.operation_portal.core.iam.model.repository.ActionRepository;
import com.thitsaworks.operation_portal.core.iam.model.repository.PrincipalRepository;
import com.thitsaworks.operation_portal.core.iam.model.repository.RoleGrantRepository;
import com.thitsaworks.operation_portal.core.iam.model.repository.RoleRepository;
import com.thitsaworks.operation_portal.core.iam.query.IAMQuery;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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

        BooleanExpression predicate = this.iamAction.isNotNull();

        var iamActions = (List<Action>) this.actionRepository.findAll(predicate);

        return iamActions.stream()
                         .map(ActionData::new)
                         .toList();
    }

    @Override
    public List<ActionData> getActionsByRole(String role) throws IAMException {

        BooleanExpression predicate1 = this.role.name.eq(role);

        var optRole = this.roleRepository.findOne(predicate1);

        if (optRole.isEmpty()) {
            throw new IAMException(IAMErrors.ROLE_NOT_FOUND);
        }

        var roleId = optRole.get()
                            .getRoleId();

        BooleanExpression predicate2 = this.roleGrant.role.roleId.eq(roleId);

        var roleGrants = (List<RoleGrant>) this.roleGrantRepository.findAll(predicate2);

        var
            actionIds =
            roleGrants.stream()
                      .map(roleGrant -> roleGrant.getAction()
                                                 .getActionId())
                      .toList();

        BooleanExpression predicate3 = this.iamAction.actionId.in(actionIds);

        var actions = (List<Action>) this.actionRepository.findAll(predicate3);

        return actions.stream()
                      .map(ActionData::new)
                      .toList();

    }

}
