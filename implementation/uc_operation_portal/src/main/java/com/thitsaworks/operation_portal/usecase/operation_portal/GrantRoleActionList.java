package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.type.ActionCode;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.util.List;

public interface GrantRoleActionList extends UseCase<GrantRoleActionList.Input, GrantRoleActionList.Output> {

    record Input(List<RoleGrant> roleGrantList) {

        public record RoleGrant(String roleName,
                                List<ActionCode> actionList) { }

    }

    record Output(boolean granted) { }

}
