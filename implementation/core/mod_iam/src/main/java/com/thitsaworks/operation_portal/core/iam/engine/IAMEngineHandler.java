package com.thitsaworks.operation_portal.core.iam.engine;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.common.identifier.RoleId;
import com.thitsaworks.operation_portal.component.common.type.ActionCode;
import com.thitsaworks.operation_portal.component.common.type.PrincipalStatus;
import com.thitsaworks.operation_portal.core.iam.data.ActionData;
import com.thitsaworks.operation_portal.core.iam.data.EngineData;
import com.thitsaworks.operation_portal.core.iam.data.PrincipalData;
import com.thitsaworks.operation_portal.core.iam.data.RoleData;
import com.thitsaworks.operation_portal.core.iam.model.repository.ActionRepository;
import com.thitsaworks.operation_portal.core.iam.model.repository.PrincipalRepository;
import com.thitsaworks.operation_portal.core.iam.model.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IAMEngineHandler implements IAMEngine {

    private static final Logger LOG = LoggerFactory.getLogger(IAMEngineHandler.class);

    private final Map<PrincipalId, PrincipalData> principalsMap = new ConcurrentHashMap<>();

    private final Map<PrincipalId, Set<RoleData>> principalRolesMap = new ConcurrentHashMap<>();

    private final Map<ActionCode, ActionData> actionCodesMap = new ConcurrentHashMap<>();

    private final Map<ActionId, ActionData> actionIdsMap = new ConcurrentHashMap<>();

    private final Map<RoleId, Set<ActionData>> roleGrantedActionsMap = new ConcurrentHashMap<>();

    private final Map<PrincipalId, Set<ActionData>> principalGrantedActionsMap = new ConcurrentHashMap<>();

    private final Map<PrincipalId, Set<ActionData>> principalDeniedActionsMap = new ConcurrentHashMap<>();

    private final PrincipalRepository principalRepository;

    private final RoleRepository roleRepository;

    private final ActionRepository actionRepository;

    @Override
    public void bootstrap() {

        this.principalsMap.clear();
        this.actionCodesMap.clear();
        this.actionIdsMap.clear();
        this.principalRolesMap.clear();
        this.principalGrantedActionsMap.clear();
        this.principalDeniedActionsMap.clear();
        this.roleGrantedActionsMap.clear();

        var principals = this.principalRepository.findAll();
        principals.forEach(principal -> this.principalsMap.put(principal.getPrincipalId(),
                                                               new PrincipalData(principal)));

        principals.forEach(principal -> {
            var roles = principal.getRoles();
            this.principalRolesMap.put(principal.getPrincipalId(),
                                       roles.stream()
                                            .map(RoleData::new)
                                            .collect(Collectors.toSet()));
        });

        principals.forEach(principal -> {
            var grantedActions = principal.getGrantedActions();
            this.principalGrantedActionsMap.put(principal.getPrincipalId(),
                                                grantedActions.stream()
                                                              .map(ActionData::new)
                                                              .collect(Collectors.toSet()));
        });

        principals.forEach(principal -> {
            var deniedActions = principal.getDeniedActions();
            this.principalDeniedActionsMap.put(principal.getPrincipalId(),
                                               deniedActions.stream()
                                                            .map(ActionData::new)
                                                            .collect(Collectors.toSet()));
        });

        var actions = this.actionRepository.findAll();
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

        LOG.info("usersMap : [{}]", this.principalsMap);
        LOG.info("userRolesMap : [{}]", this.principalRolesMap);
        LOG.info("userGrantedActionsMap : [{}]", this.principalGrantedActionsMap);
        LOG.info("userDeniedActionsMap : [{}]", this.principalDeniedActionsMap);
        LOG.info("actionCodesMap : [{}]", this.actionCodesMap);
    }

    @Override
    public EngineData dumpEngineState() {

        return new EngineData(Collections.unmodifiableMap(this.principalsMap),
                              Collections.unmodifiableMap(this.principalRolesMap),
                              Collections.unmodifiableMap(this.actionCodesMap),
                              Collections.unmodifiableMap(this.actionIdsMap),
                              Collections.unmodifiableMap(this.roleGrantedActionsMap),
                              Collections.unmodifiableMap(this.principalGrantedActionsMap),
                              Collections.unmodifiableMap(this.principalDeniedActionsMap));
    }

    @Override
    public List<ActionData> getActions() {

        return new ArrayList<>(this.actionCodesMap.values());
    }

    @Override
    public boolean isGrantedAction(PrincipalId principalId, ActionCode actionCode) {

        var principal = this.principalsMap.get(principalId);
        var denialActions = this.principalDeniedActionsMap.get(principalId);
        var grantedActions = this.principalGrantedActionsMap.get(principalId);
        var principalRoles = Optional.ofNullable(this.principalRolesMap.get(principalId))
                                     .orElse(Set.of())
                                     .stream()
                                     .filter(RoleData::active)
                                     .toList();

        List<ActionData> roleGrantedActions = new ArrayList<>();
        for (var userRole : principalRoles) {
            roleGrantedActions.addAll(this.roleGrantedActionsMap.get(userRole.roleId()));
        }

        var actionData = this.actionCodesMap.get(actionCode);

        if (principal != null) {

            if (!principal.principalStatus()
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
    public boolean isGrantedAction(PrincipalId principalId, ActionId actionId) {

        return false;
    }

    @Override
    public List<RoleData> getRolesByPrincipal(PrincipalId principalId) {

        var roleSet = this.principalRolesMap.get(principalId);

        var roleList = new ArrayList<RoleData>();

        for (var role : roleSet) {

            if (role.active()) {
                roleList.add(role);
            }
        }

        return roleList;
    }

    @Override
    public PrincipalData getPrincipal(PrincipalId principalId) {

        return this.principalsMap.get(principalId);
    }

    @Override
    public void addPrincipal(PrincipalId principalId, PrincipalData principalData) {

        this.principalsMap.put(principalId, principalData);

    }

    @Override
    public void removePrincipal(PrincipalId principalId, PrincipalData principalData) {

        this.principalsMap.remove(principalId);
    }

    @Override
    public void addPrincipalRole(PrincipalId principalId, RoleData roleData) {

        this.principalRolesMap.computeIfAbsent(principalId, k -> new HashSet<>())
                              .add(roleData);

    }

    @Override
    public void removePrincipalRole(PrincipalId principalId, RoleData roleData) {

        this.principalRolesMap.computeIfAbsent(principalId, k -> new HashSet<>())
                              .remove(roleData);

    }

    @Override
    public void addAction(ActionId actionId, ActionCode actionCode, ActionData actionData) {

        this.actionCodesMap.put(actionCode, actionData);
        this.actionIdsMap.put(actionId, actionData);
    }

    @Override
    public void removeAction(ActionId actionId, ActionCode actionCode, ActionData actionData) {

        this.actionCodesMap.remove(actionCode);
        this.actionIdsMap.remove(actionId);

    }

    @Override
    public void addRoleGrantedAction(RoleId roleId, ActionData actionData) {

        this.roleGrantedActionsMap.computeIfAbsent(roleId, k -> new HashSet<>())
                                  .add(actionData);
    }

    @Override
    public void removeRoleGrantedAction(RoleId roleId, ActionData actionData) {

        if (actionData == null) {
            this.roleGrantedActionsMap.remove(roleId);

        } else {
            this.roleGrantedActionsMap.computeIfAbsent(roleId, k -> new HashSet<>())
                                      .remove(actionData);
        }

    }

    @Override
    public void addPrincipalGrantedAction(PrincipalId principalId, ActionData actionData) {

        this.principalGrantedActionsMap.computeIfAbsent(principalId, k -> new HashSet<>())
                                       .add(actionData);
    }

    @Override
    public void removePrincipalGrantedAction(PrincipalId principalId, ActionData actionData) {

        this.principalGrantedActionsMap.computeIfAbsent(principalId, k -> new HashSet<>())
                                       .remove(actionData);
    }

    @Override
    public void addPrincipalDeniedAction(PrincipalId principalId, ActionData actionData) {

        this.principalDeniedActionsMap.computeIfAbsent(principalId, k -> new HashSet<>())
                                      .add(actionData);
    }

    @Override
    public void removePrincipalDeniedAction(PrincipalId principalId, ActionData actionData) {

        this.principalDeniedActionsMap.computeIfAbsent(principalId, k -> new HashSet<>())
                                      .remove(actionData);
    }

    @Override
    public Map<PrincipalId, PrincipalData> getPrincipalsMap() {

        return Collections.unmodifiableMap((this.principalsMap));
    }

    @Override
    public Map<PrincipalId, Set<RoleData>> getPrincipalRolesMap() {

        return Collections.unmodifiableMap(this.principalRolesMap);
    }

    @Override
    public Map<ActionCode, ActionData> getActionCodesMap() {

        return Collections.unmodifiableMap(this.actionCodesMap);
    }

    @Override
    public Map<ActionId, ActionData> getActionIdsMap() {

        return Collections.unmodifiableMap(this.actionIdsMap);
    }

    @Override
    public Map<RoleId, Set<ActionData>> getRoleGrantedActionsMap() {

        return Collections.unmodifiableMap(this.roleGrantedActionsMap);
    }

    @Override
    public Map<PrincipalId, Set<ActionData>> getPrincipalGrantedActionsMap() {

        return Collections.unmodifiableMap(this.principalGrantedActionsMap);
    }

    @Override

    public Map<PrincipalId, Set<ActionData>> getPrincipalDeniedActionsMap() {

        return Collections.unmodifiableMap(this.principalDeniedActionsMap);
    }

    @Override
    public ActionData getAction(ActionCode actionCode) {

        if (this.actionCodesMap.containsKey(actionCode)) {
            return this.actionCodesMap.get(actionCode);
        }

        return null;
    }

}
