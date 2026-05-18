package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.misc.annotation.ActionMetadata;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.util.ActionCategory;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.data.ActionData;
import com.thitsaworks.operation_portal.core.iam.query.IAMQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetActionListByRole;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.ConnectException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@ActionMetadata(
    category = ActionCategory.ROLE_MENU_PERMISSION_IAM,
    isMandatory = true)
public class GetActionListByRoleHandler
    extends OperationPortalUseCase<GetActionListByRole.Input, GetActionListByRole.Output>
    implements GetActionListByRole {

    private static final Logger LOG = LoggerFactory.getLogger(GetActionListByRoleHandler.class);

    private final IAMQuery iamQuery;

    public GetActionListByRoleHandler(PrincipalCache principalCache,
                                      ActionAuthorizationManager actionAuthorizationManager,
                                      IAMQuery iamQuery) {

        super(principalCache, actionAuthorizationManager);

        this.iamQuery = iamQuery;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException, ConnectException {

        List<ActionData> actionList = this.iamQuery.getActionList();

        List<ActionData> actionOptionList = this.iamQuery.getActionListByRole(input.roleId());

        Set<ActionId> selectedIds = actionOptionList
                                        .stream()
                                        .map(ActionData::actionId)
                                        .collect(Collectors.toSet());

        List<Output.ActionOption> result = actionList.stream().map(action -> {
            String code = action.actionCode().getValue();

            boolean selected = selectedIds.contains(action.actionId());
            boolean mandatory = selected && action.isMandatory();

            return new Output.ActionOption(
                action.actionId(), code, action.category(), selected, mandatory);
        }).toList();

        return new Output(result);
    }

}
