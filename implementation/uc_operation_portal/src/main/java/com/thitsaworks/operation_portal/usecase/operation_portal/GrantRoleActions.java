package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.type.iamtesttype.ActionCode;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.util.List;

public interface GrantRoleActions extends UseCase<GrantRoleActions.Input, GrantRoleActions.Output> {

    record Input(List<SingleRoleGrant> singleRoleGrantList) {

        public record SingleRoleGrant(String role,
                                      List<ActionCode> actionList) { }

    }

    record Output(boolean granted) { }

}
