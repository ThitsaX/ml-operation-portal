package com.thitsaworks.operation_portal.core.test_iam;

import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.identifier.iamtestid.RoleId;
import com.thitsaworks.operation_portal.component.common.type.iamtesttype.ActionCode;
import com.thitsaworks.operation_portal.core.test_iam.data.UserData;
import com.thitsaworks.operation_portal.core.test_iam.model.BlockedAction;
import com.thitsaworks.operation_portal.core.test_iam.model.IAMAction;
import com.thitsaworks.operation_portal.core.test_iam.model.Role;
import com.thitsaworks.operation_portal.core.test_iam.model.RoleGrant;
import com.thitsaworks.operation_portal.core.test_iam.model.User;
import com.thitsaworks.operation_portal.core.test_iam.model.UserGrant;
import com.thitsaworks.operation_portal.core.test_iam.model.UserRole;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


@Component
public class IAMEngine {

    private static final Logger LOG = LoggerFactory.getLogger(IAMEngine.class);

    public  final Map<UserId, UserData> usersMap = new ConcurrentHashMap<>();

    public  final Map<UserId, Set<Role>> userRolesMap = new ConcurrentHashMap<>();
    public  final Map<ActionCode, IAMAction> actionMap = new ConcurrentHashMap<>();

    public  final Map<RoleId, Set<IAMAction>> roleGrantedActionsMap = new ConcurrentHashMap<>();

    public  final Map<UserId, Set<IAMAction>> userGrantedActionsMap = new ConcurrentHashMap<>();

    public  final Map<UserId, Set<IAMAction>> userDeniedActionsMap = new ConcurrentHashMap<>();

    public void print(){
        LOG.info("usersMap : {}", usersMap);
        LOG.info("userRolesMap : {}", userRolesMap);
        LOG.info("userGrantedActionsMap : {}", userGrantedActionsMap);
        LOG.info("userDeniedActionsMap : {}", userDeniedActionsMap);
    }

   public class Updater {

        @PostPersist
        @PostUpdate
        public void persistOrUpdate(User user) {

            UserData userData = new UserData(user.getUserId(),
                                             user.getAccessKey(),
                                             user.getSecretKey(),
                                             user.getRealmType(),
                                             user.getRealmId(),
                                             user.getPrincipalStatus());

            usersMap.put(user.getUserId(), userData);

            userRolesMap.computeIfAbsent(user.getUserId(), k -> new java.util.HashSet<>())
                        .addAll(user.getRoles());

            for (var actions : user.getGrantedActions()) {
                userGrantedActionsMap.computeIfAbsent(user.getUserId(), k -> new java.util.HashSet<>())
                                     .add(actions);
            }

            for (var actions : user.getDeniedActions()) {
                userDeniedActionsMap.computeIfAbsent(user.getUserId(), k -> new java.util.HashSet<>())
                                    .add(actions);
            }

        }

        @PostRemove
        public void postRemove(User user) {

            usersMap.remove(user.getUserId());
            userRolesMap.remove(user.getUserId());
            userGrantedActionsMap.remove(user.getUserId());
            userDeniedActionsMap.remove(user.getUserId());

        }

        @PostPersist
        @PostUpdate
        public void persistOrUpdate(Role role) {

            var roleGrants = role.getGrantedActions();

            roleGrantedActionsMap.computeIfAbsent(role.getRoleId(), k -> new java.util.HashSet<>())
                                 .addAll(roleGrants);

        }

        @PostRemove
        public void postRemove(Role role) {

            var
                userIdList =
                userRolesMap.entrySet()
                            .stream()
                            .filter(entry -> entry.getValue()
                                                  .contains(role))
                            .map(Map.Entry::getKey)
                            .collect(Collectors.toSet());

            roleGrantedActionsMap.remove(role.getRoleId());

            for (var userId : userIdList) {
                userRolesMap.get(userId)
                            .remove(role);
            }

        }

        @PostPersist
        @PostUpdate
        public void persistOrUpdate(UserRole userRole) {

            var role = userRole.getRole();
            var userId = userRole.getUser()
                                 .getUserId();

            userRolesMap.computeIfAbsent(userId, k -> new java.util.HashSet<>())
                        .add(role);

        }

        @PostRemove
        public void postRemove(UserRole userRole) {

            var role = userRole.getRole();
            var userId = userRole.getUser()
                                 .getUserId();

            userRolesMap.computeIfAbsent(userId, k -> new java.util.HashSet<>())
                        .remove(role);

        }

        @PostPersist
        @PostUpdate
        public void persistOrUpdate(BlockedAction blockedAction) {

            var userId = blockedAction.getUser()
                                      .getUserId();

            userDeniedActionsMap.computeIfAbsent(userId, k -> new java.util.HashSet<>())
                                .add(blockedAction.getIAMAction());

        }

        @PostRemove
        public void postRemove(BlockedAction blockedAction) {

            var userId = blockedAction.getUser()
                                      .getUserId();

            userDeniedActionsMap.computeIfAbsent(userId, k -> new java.util.HashSet<>())
                                .remove(blockedAction.getIAMAction());
        }

        @PostPersist
        @PostUpdate
        public void persistOrUpdate(RoleGrant roleGrant) {

            var role = roleGrant.getRole();

            roleGrantedActionsMap.computeIfAbsent(role.getRoleId(), k -> new java.util.HashSet<>())
                                 .add(roleGrant.getIAMAction());

        }

        @PostRemove
        public void postRemove(RoleGrant roleGrant) {

            var role = roleGrant.getRole();

            roleGrantedActionsMap.computeIfAbsent(role.getRoleId(), k -> new java.util.HashSet<>())
                                 .remove(roleGrant.getIAMAction());
        }

        @PostPersist
        @PostUpdate
        public void persistOrUpdate(UserGrant userGrant) {

            var userId = userGrant.getUser().getUserId();
            var iamAction = userGrant.getIAMAction();

            userGrantedActionsMap.computeIfAbsent(userId, k -> new java.util.HashSet<>())
                                 .add(iamAction);
        }

        @PostRemove
        public void postRemove(UserGrant userGrant) {

            var userId = userGrant.getUser().getUserId();
            var iamAction = userGrant.getIAMAction();

            userGrantedActionsMap.computeIfAbsent(userId, k -> new java.util.HashSet<>())
                                 .remove(iamAction);
        }

        @PostPersist
        @PostUpdate
        public void persistOrUpdate(IAMAction iamAction) {

            actionMap.put(iamAction.getActionCode(), iamAction);

        }

        @PostRemove
        public void postRemove(IAMAction iamAction) {

            actionMap.remove(iamAction.getActionCode());

        }



    }


    }
