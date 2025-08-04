package com.thitsaworks.operation_portal.core.test_iam.engine;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.identifier.RoleId;
import com.thitsaworks.operation_portal.component.common.type.PrincipalStatus;
import com.thitsaworks.operation_portal.component.common.type.ActionCode;
import com.thitsaworks.operation_portal.core.test_iam.data.ActionData;
import com.thitsaworks.operation_portal.core.test_iam.data.RoleData;
import com.thitsaworks.operation_portal.core.test_iam.data.UserData;
import com.thitsaworks.operation_portal.core.test_iam.model.repository.IAMActionRepository;
import com.thitsaworks.operation_portal.core.test_iam.model.repository.RoleRepository;
import com.thitsaworks.operation_portal.core.test_iam.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IAMEngineHandler implements IAMEngine {

    private static final Logger LOG = LoggerFactory.getLogger(IAMEngineHandler.class);

    private final Map<UserId, UserData> usersMap = new ConcurrentHashMap<>();

    private final Map<UserId, Set<RoleData>> userRolesMap = new ConcurrentHashMap<>();

    private final Map<ActionCode, ActionData> actionCodesMap = new ConcurrentHashMap<>();

    private final Map<ActionId, ActionData> actionIdsMap = new ConcurrentHashMap<>();

    private final Map<RoleId, Set<ActionData>> roleGrantedActionsMap = new ConcurrentHashMap<>();

    private final Map<UserId, Set<ActionData>> userGrantedActionsMap = new ConcurrentHashMap<>();

    private final Map<UserId, Set<ActionData>> userDeniedActionsMap = new ConcurrentHashMap<>();

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final IAMActionRepository iamActionRepository;

    @Override
    public void bootstrap() {

        this.usersMap.clear();
        this.actionCodesMap.clear();
        this.actionIdsMap.clear();
        this.userRolesMap.clear();
        this.userGrantedActionsMap.clear();
        this.userDeniedActionsMap.clear();
        this.roleGrantedActionsMap.clear();

        var users = this.userRepository.findAll();
        users.forEach(user -> this.usersMap.put(user.getUserId(), new UserData(user)));

        users.forEach(user -> {
            var roles = user.getRoles();
            this.userRolesMap.put(user.getUserId(),
                                  roles.stream()
                                       .map(RoleData::new)
                                       .collect(Collectors.toSet()));
        });

        users.forEach(user -> {
            var grantedActions = user.getGrantedActions();
            this.userGrantedActionsMap.put(user.getUserId(),
                                           grantedActions.stream()
                                                         .map(ActionData::new)
                                                         .collect(Collectors.toSet()));
        });

        users.forEach(user -> {
            var deniedActions = user.getDeniedActions();
            this.userDeniedActionsMap.put(user.getUserId(),
                                          deniedActions.stream()
                                                       .map(ActionData::new)
                                                       .collect(Collectors.toSet()));
        });

        var actions = this.iamActionRepository.findAll();
        actions.forEach(action -> {
            this.actionCodesMap.put(action.getActionCode(), new ActionData(action));
            this.actionIdsMap.put(action.getActionId(), new ActionData(action));
        });

        var roles = this.roleRepository.findAll();
        roles.forEach(role -> {
            var grantedActions = role.getGrantedActions();
            this.roleGrantedActionsMap.put(role.getRoleId(),
                                           grantedActions.stream()
                                                         .map(ActionData::new)
                                                         .collect(Collectors.toSet()));
        });

    }

    @Override
    public void print() {

        LOG.info("usersMap : [{}]", this.usersMap);
        LOG.info("userRolesMap : [{}]", this.userRolesMap);
        LOG.info("userGrantedActionsMap : [{}]", this.userGrantedActionsMap);
        LOG.info("userDeniedActionsMap : [{}]", this.userDeniedActionsMap);
        LOG.info("actionCodesMap : [{}]", this.actionCodesMap);
    }

    @Override
    public boolean isGrantedAction(UserId userId, ActionCode actionCode) {

        var user = this.usersMap.get(userId);
        var denialActions = this.userDeniedActionsMap.get(userId);
        var grantedActions = this.userGrantedActionsMap.get(userId);
        var userRoles = this.userRolesMap.get(userId);

        List<ActionData> roleGrantedActions = new ArrayList<>();
        for (var userRole : userRoles) {
            roleGrantedActions.addAll(this.roleGrantedActionsMap.get(userRole.roleId()));
        }

        var actionData = this.actionCodesMap.get(actionCode);

        if (user != null) {

            if (!user.principalStatus()
                     .equals(PrincipalStatus.ACTIVE)) { return false; }

            if (denialActions != null) {
                if (denialActions.stream()
                                 .anyMatch(action -> action.actionId()
                                                           .equals(actionData.actionId()))) {
                    return false;
                }
            }

            if (grantedActions != null) {
                if (grantedActions.stream()
                                  .anyMatch(action -> action.actionId()
                                                            .equals(actionData.actionId()))) {
                    return true;
                }
            }

            return roleGrantedActions.stream()
                                     .anyMatch(action -> action.actionId()
                                                               .equals(actionData.actionId()));
        }

        return false;
    }

    @Override
    public boolean isGrantedAction(UserId userId, ActionId actionId) {

        return false;
    }

    @Override
    public Map<UserId, UserData> getUsersMap() {

        return this.usersMap;
    }

    @Override
    public Map<UserId, Set<RoleData>> getUserRolesMap() {

        return this.userRolesMap;
    }

    @Override
    public Map<ActionCode, ActionData> getActionCodesMap() {

        return this.actionCodesMap;
    }

    @Override
    public Map<ActionId, ActionData> getActionIdsMap() {

        return this.actionIdsMap;
    }

    @Override
    public Map<RoleId, Set<ActionData>> getRoleGrantedActionsMap() {

        return this.roleGrantedActionsMap;
    }

    @Override
    public Map<UserId, Set<ActionData>> getUserGrantedActionsMap() {

        return this.userGrantedActionsMap;
    }

    @Override
    public Map<UserId, Set<ActionData>> getUserDeniedActionsMap() {

        return this.userDeniedActionsMap;
    }

}
