package com.thitsaworks.operation_portal.core.iam.query.impl.jpa;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
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
import com.thitsaworks.operation_portal.core.iam.model.repository.PrincipalRepository;
import com.thitsaworks.operation_portal.core.iam.model.repository.RoleGrantRepository;
import com.thitsaworks.operation_portal.core.iam.model.repository.RoleRepository;
import com.thitsaworks.operation_portal.core.iam.query.IAMQuery;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public Map<List<MenuData>, List<ActionData>> getMenusAndActionsByUserId(PrincipalId principalId) {

        // get roles for the principal
        List<RoleId> roleIds = this.readQueryFactory.select(principalRole.role.roleId).from(principalRole).where(
                principalRole.principal.principalId.eq(principalId)).fetch();

        // get role grants for the principal
        List<Tuple> roleGrants = this.readQueryFactory.select(roleGrant.Action.actionId,
                                                              Expressions.constant(principalId),
                                                              roleGrant.role.roleId)
                                                      .from(roleGrant)
                                                      .where(roleGrant.role.roleId.in(roleIds))
                                                      .fetch();

        // get principal grants for the principal
        List<Tuple> principalGrants = this.readQueryFactory.select(principalGrant.Action.actionId,
                                                                   principalGrant.principal.principalId,
                                                                   Expressions.constant(0L)).from(principalGrant).where(
                                                  principalGrant.principal.principalId.eq(principalId))
                                                           .fetch();

        // merge the two lists and remove duplicates
        Set<Tuple> grantedActions = new HashSet<>();
        grantedActions.addAll(roleGrants);
        grantedActions.addAll(principalGrants);

        // create a map of menus to their associated actions
        Map<List<MenuData>, List<ActionData>> resultMap = new HashMap<>();

        for (Tuple granted : grantedActions) {

            ActionId actionId = granted.get(0, ActionId.class);
            RoleId roleId = granted.get(2, RoleId.class);

            // get the menu data and action data for the granted action
            List<Tuple> rows = readQueryFactory.select(menu.menuId, menu.name, action.actionId, action.actionCode)
                                               .from(action)
                                               .join(menuGrant)
                                               .on(menuGrant.Action.actionId.eq(action.actionId))
                                               .join(menu)
                                               .on(menu.menuId.eq(menuGrant.menu.menuId).and(menu.isActive.eq(true)))
                                               .join(qUr)
                                               .on(qUr.principal.principalId.eq(principalId))
                                               .join(role)
                                               .on(role.roleId.eq(qUr.role.roleId).and(role.roleId.eq(roleId)))
                                               .where(action.actionId.eq(actionId)
                                                                     .and(action.actionId.notIn(JPAExpressions.select(
                                                                                                                      blockedAction.Action.actionId)
                                                                                                              .from(blockedAction)
                                                                                                              .where(blockedAction.principal.principalId.eq(
                                                                                                                        principalId)))))
                                               .fetch();

            // create a partial map
            Map<List<MenuData>, List<ActionData>> partialMap =
                    rows.stream().collect(Collectors.groupingBy(row -> List.of(new MenuData(row.get(menu.menuId),
                                                                                            row.get(menu.name),
                                                    "0",
                                                    true)),
                                                                Collectors.mapping(row -> new ActionData(row.get(action.actionId),
                                                                                                         row.get(action.actionCode),
                                                                                                         "",
                                                                                                         ""),
                                                                                   Collectors.toList())
                                                  ));

            // merge the partial map with the result map
            partialMap.forEach((menuList, actionList) ->
                                   resultMap.merge(menuList, actionList, (oldList, newList) -> {
                                       Set<ActionData> merged = new LinkedHashSet<>(oldList);
                                       merged.addAll(newList);
                                       return new ArrayList<>(merged);
                                   })
                              );

        }

        return resultMap;
    }
    /* <<<<<<<<<<  23d64a55-83cb-413a-9adb-389860137df9  >>>>>>>>>>> */

}
