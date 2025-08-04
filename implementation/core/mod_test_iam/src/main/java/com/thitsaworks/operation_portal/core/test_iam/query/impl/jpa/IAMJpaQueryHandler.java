package com.thitsaworks.operation_portal.core.test_iam.query.impl.jpa;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.thitsaworks.operation_portal.core.test_iam.data.ActionData;
import com.thitsaworks.operation_portal.core.test_iam.data.RoleData;
import com.thitsaworks.operation_portal.core.test_iam.data.UserData;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.test_iam.model.IAMAction;
import com.thitsaworks.operation_portal.core.test_iam.model.QIAMAction;
import com.thitsaworks.operation_portal.core.test_iam.model.QRole;
import com.thitsaworks.operation_portal.core.test_iam.model.QRoleGrant;
import com.thitsaworks.operation_portal.core.test_iam.model.QUser;
import com.thitsaworks.operation_portal.core.test_iam.model.Role;
import com.thitsaworks.operation_portal.core.test_iam.model.RoleGrant;
import com.thitsaworks.operation_portal.core.test_iam.model.User;
import com.thitsaworks.operation_portal.core.test_iam.model.repository.IAMActionRepository;
import com.thitsaworks.operation_portal.core.test_iam.model.repository.RoleGrantRepository;
import com.thitsaworks.operation_portal.core.test_iam.model.repository.RoleRepository;
import com.thitsaworks.operation_portal.core.test_iam.model.repository.UserRepository;
import com.thitsaworks.operation_portal.core.test_iam.query.IAMQuery;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IAMJpaQueryHandler implements IAMQuery {

    private static final Logger LOG = LoggerFactory.getLogger(IAMJpaQueryHandler.class);

    private final QIAMAction iamAction = QIAMAction.iAMAction;

    private final QRole role = QRole.role;

    private final QUser user = QUser.user;

    private final QRoleGrant roleGrant = QRoleGrant.roleGrant;

    private final IAMActionRepository iamActionRepository;

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    private final RoleGrantRepository roleGrantRepository;

    @Override
    public List<RoleData> getRoles() {

        BooleanExpression predicate = this.role.isNotNull();

        var roles = (List<Role>) this.roleRepository.findAll(predicate);

        return roles.stream()
                    .map(RoleData::new)
                    .toList();
    }

    @Override
    public List<UserData> getUsers() {

        BooleanExpression predicate = this.user.isNotNull();

        var users = (List<User>) this.userRepository.findAll(predicate);

        return users.stream()
                    .map(UserData::new)
                    .toList();
    }

    @Override
    public List<ActionData> getActions() {

        BooleanExpression predicate = this.iamAction.isNotNull();

        var iamActions = (List<IAMAction>) this.iamActionRepository.findAll(predicate);

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
                      .map(roleGrant -> roleGrant.getIAMAction()
                                                 .getActionId())
                      .toList();

        BooleanExpression predicate3 = this.iamAction.actionId.in(actionIds);

        var actions = (List<IAMAction>) this.iamActionRepository.findAll(predicate3);

        return actions.stream()
                      .map(ActionData::new)
                      .toList();

    }

}
