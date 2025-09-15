package com.thitsaworks.operation_portal.core.iam.query.impl.jpa;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.MenuId;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.common.identifier.RoleId;
import com.thitsaworks.operation_portal.core.iam.data.ActionData;
import com.thitsaworks.operation_portal.core.iam.data.MenuData;
import com.thitsaworks.operation_portal.core.iam.data.PrincipalData;
import com.thitsaworks.operation_portal.core.iam.data.RoleData;
import com.thitsaworks.operation_portal.core.iam.engine.IAMEngine;
import com.thitsaworks.operation_portal.core.iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.iam.model.Action;
import com.thitsaworks.operation_portal.core.iam.model.Menu;
import com.thitsaworks.operation_portal.core.iam.model.MenuGrant;
import com.thitsaworks.operation_portal.core.iam.model.Principal;
import com.thitsaworks.operation_portal.core.iam.model.QAction;
import com.thitsaworks.operation_portal.core.iam.model.QBlockedAction;
import com.thitsaworks.operation_portal.core.iam.model.QMenu;
import com.thitsaworks.operation_portal.core.iam.model.QMenuGrant;
import com.thitsaworks.operation_portal.core.iam.model.QPrincipal;
import com.thitsaworks.operation_portal.core.iam.model.QPrincipalGrant;
import com.thitsaworks.operation_portal.core.iam.model.QPrincipalRole;
import com.thitsaworks.operation_portal.core.iam.model.QRole;
import com.thitsaworks.operation_portal.core.iam.model.QRoleGrant;
import com.thitsaworks.operation_portal.core.iam.model.Role;
import com.thitsaworks.operation_portal.core.iam.model.repository.ActionRepository;
import com.thitsaworks.operation_portal.core.iam.model.repository.MenuGrantRepository;
import com.thitsaworks.operation_portal.core.iam.model.repository.MenuRepository;
import com.thitsaworks.operation_portal.core.iam.model.repository.PrincipalRepository;
import com.thitsaworks.operation_portal.core.iam.model.repository.RoleGrantRepository;
import com.thitsaworks.operation_portal.core.iam.model.repository.RoleRepository;
import com.thitsaworks.operation_portal.core.iam.query.IAMQuery;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IAMJpaQueryHandler implements IAMQuery {

    private static final Logger LOG = LoggerFactory.getLogger(IAMJpaQueryHandler.class);

    private final QAction action = QAction.action;

    private final QRole role = QRole.role;

    private final QPrincipal principal = QPrincipal.principal;

    private final QRoleGrant roleGrant = QRoleGrant.roleGrant;

    private final QPrincipalRole principalRole = QPrincipalRole.principalRole;

    private final QPrincipalGrant principalGrant = QPrincipalGrant.principalGrant;

    private final QMenuGrant menuGrant = QMenuGrant.menuGrant;

    private final QMenu menu = QMenu.menu;

    private final QPrincipalRole qUr = QPrincipalRole.principalRole;

    private final QBlockedAction blockedAction = QBlockedAction.blockedAction;

    private final ActionRepository actionRepository;

    private final RoleRepository roleRepository;

    private final PrincipalRepository principalRepository;

    private final RoleGrantRepository roleGrantRepository;

    private final MenuRepository menuRepository;

    private final MenuGrantRepository menuGrantRepository;

    private final IAMEngine iamEngine;

    private final JPAQueryFactory readQueryFactory;

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

        BooleanExpression filter = this.action.isNotNull();
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

    @Override
    public List<RoleData> getRolesByPrincipal(PrincipalId principalId) throws IAMException {

        var availablePrincipal = this.iamEngine.getPrincipal(principalId);

        if (availablePrincipal != null) {
            return this.iamEngine.getRolesByPrincipal(principalId);
        }

        var principal = this.principalRepository.findById(principalId)
                                                .orElseThrow(() -> new IAMException(IAMErrors.PRINCIPAL_NOT_FOUND));

        var roles = principal.getRoles();

        this.iamEngine.addPrincipal(principalId, new PrincipalData(principal));

        List<RoleData> roleDataList = roles.stream()
                                           .map(RoleData::new)
                                           .collect(Collectors.toList());

        roleDataList.forEach(roleData -> this.iamEngine.addPrincipalRole(principalId, roleData));

        return roleDataList;

    }

    /* <<<<<<<<<<<<<<  ✨ Windsurf Command 🌟 >>>>>>>>>>>>>>>> */

    /**
     * @param principalId principalId to retrieve menus and actions for
     * @return a map where the key is a list of menu data and the value is a list of action data associated with the
     * menus
     */
    @Override
    public Map<List<MenuData>, List<ActionData>> getMenusAndActionsByUserId(PrincipalId principalId)
        throws IAMException {

        var principal = this.principalRepository.findByPrincipalId(principalId).orElseThrow(() -> new IAMException(IAMErrors.PRINCIPAL_NOT_FOUND));

        // get roles for the principal
        var roleList = principal.getRoles();

        // get grantedActions which not include blockedActions
        Set<Action> grantedActionList = new HashSet<>();
        for(var role : roleList){
            grantedActionList.addAll(role.getGrantedActions());
        }

        Set<Action> blockedActionList = new HashSet<>(principal.getDeniedActions());
        grantedActionList.removeAll(blockedActionList);

        BooleanExpression predicate1  = this.menuGrant.Action.actionId.in(grantedActionList.stream().map(Action::getActionId).toList());
        var menuGrantList = (List<MenuGrant>) this.menuGrantRepository.findAll(predicate1);

        Set<Menu> menuList = menuGrantList.stream().map(MenuGrant::getMenu).collect(Collectors.toSet());

        List<MenuId>  menuIdList = menuList.stream().map(menu -> new MenuId(Long.parseLong(menu.getParentId()))).toList();

        BooleanExpression predicate2 = this.menu.menuId.in(menuIdList);
        var parentMenuList = (List<Menu>) this.menuRepository.findAll(predicate2);

        menuList.addAll(parentMenuList);

        List<MenuData> menuDataList = menuList.stream().map(MenuData::new).toList();
        List<ActionData> grantedActionDataList = grantedActionList.stream().map(ActionData::new).toList();


        Map<List<MenuData>, List<ActionData>> result = new HashMap<>();
        result.put(menuDataList, grantedActionDataList);

        return result;

    }
    /* <<<<<<<<<<  23d64a55-83cb-413a-9adb-389860137df9  >>>>>>>>>>> */

    @Override
    public List<ActionData> getGrantedActionsByPrincipal(PrincipalId principalId) throws IAMException {

        BooleanExpression predicate1 = this.principal.principalId.eq(principalId);

        var
            principal =
            this.principalRepository.findOne(predicate1)
                                    .orElseThrow(() -> new IAMException(IAMErrors.PRINCIPAL_NOT_FOUND));

        var
            roleIdList =
            principal.getRoles()
                     .stream()
                     .map(Role::getRoleId)
                     .toList();

        BooleanExpression predicate2 = this.role.roleId.in(roleIdList);

        var roleList = (List<Role>) this.roleRepository.findAll(predicate2);

        var grantedActionList = roleList.stream()
                                        .map(Role::getGrantedActions)
                                        .flatMap(Collection::stream)
                                        .collect(Collectors.toCollection(HashSet::new));

        var blockedActionList = principal.getDeniedActions();

        for (Action blockedAction : blockedActionList) {
            grantedActionList.remove(blockedAction);
        }

        return grantedActionList.stream()
                                .map(ActionData::new)
                                .toList();
    }

}
