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

    private final QAction iamAction = QAction.action;

    private final QRole role = QRole.role;

    private final QPrincipal principal = QPrincipal.principal;

    private final QRoleGrant roleGrant = QRoleGrant.roleGrant;

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

    @Override
    public Map<List<MenuData>, List<ActionData>> getMenusAndActionsByUserId(PrincipalId principalId) {

        QPrincipalRole qPrincipalRole = QPrincipalRole.principalRole;
        QRoleGrant qRoleGrant = QRoleGrant.roleGrant;
        QPrincipalGrant qPrincipalGrant = QPrincipalGrant.principalGrant;
        QAction qAction = QAction.action;
        QMenuGrant qMenuGrant = QMenuGrant.menuGrant;
        QMenu qMenu = QMenu.menu;
        QPrincipalRole qUr = QPrincipalRole.principalRole;
        QRole qRole = QRole.role;
        QBlockedAction qBlockedAction = QBlockedAction.blockedAction;

        List<RoleId>
            roleIds =
            this.readQueryFactory.select(qPrincipalRole.role.roleId)
                                 .from(qPrincipalRole)
                                 .where(
                                     qPrincipalRole.principal.principalId.eq(principalId))
                                 .fetch();

        List<Tuple> roleGrants = this.readQueryFactory.select(qRoleGrant.Action.actionId,
                                                              Expressions.constant(principalId),
                                                              qRoleGrant.role.roleId)
                                                      .from(qRoleGrant)
                                                      .where(qRoleGrant.role.roleId.in(roleIds))
                                                      .fetch();

        List<Tuple> principalGrants = this.readQueryFactory.select(qPrincipalGrant.Action.actionId,
                                                                   qPrincipalGrant.principal.principalId,
                                                                   Expressions.constant(0L)) // role_id = 0
                                                           .from(qPrincipalGrant)
                                                           .where(qPrincipalGrant.principal.principalId.eq(principalId))
                                                           .fetch();

        Set<Tuple> grantedActions = new HashSet<>();
        grantedActions.addAll(roleGrants);
        grantedActions.addAll(principalGrants);

        Map<List<MenuData>, List<ActionData>> resultMap = new HashMap<>();

        for (Tuple granted : grantedActions) {

            ActionId actionId = granted.get(0, ActionId.class);
            RoleId roleId = granted.get(2, RoleId.class);

            List<Tuple> rows = readQueryFactory.select(qMenu.menuId, qMenu.name, qAction.actionId, qAction.actionCode)
                                               .from(qAction)
                                               .join(qMenuGrant)
                                               .on(qMenuGrant.Action.actionId.eq(qAction.actionId))
                                               .join(qMenu)
                                               .on(qMenu.menuId.eq(qMenuGrant.menu.menuId)
                                                               .and(qMenu.isActive.eq(true)))
                                               .join(qUr)
                                               .on(qUr.principal.principalId.eq(principalId))
                                               .join(qRole)
                                               .on(qRole.roleId.eq(qUr.role.roleId)
                                                               .and(qRole.roleId.eq(roleId)))
                                               .where(qAction.actionId.eq(actionId)
                                                                      .and(qAction.actionId.notIn(JPAExpressions.select(
                                                                                                                    qBlockedAction.Action.actionId)
                                                                                                                .from(
                                                                                                                    qBlockedAction)
                                                                                                                .where(
                                                                                                                    qBlockedAction.principal.principalId.eq(
                                                                                                                        principalId)))))
                                               .fetch();

            Map<List<MenuData>, List<ActionData>> partialMap =
                rows.stream()
                    .collect(Collectors.groupingBy(
                        row -> List.of(new MenuData(row.get(qMenu.menuId),
                                                    row.get(qMenu.name),
                                                    "0",
                                                    true)),
                        Collectors.mapping(row -> new ActionData(row.get(qAction.actionId),
                                                                 row.get(qAction.actionCode),
                                                                 "",
                                                                 ""),
                                           Collectors.toList())
                                                  ));

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

}
