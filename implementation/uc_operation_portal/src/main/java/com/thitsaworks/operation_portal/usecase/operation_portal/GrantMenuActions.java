package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.type.iamtesttype.ActionCode;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.util.List;

public interface GrantMenuActions extends UseCase<GrantMenuActions.Input, GrantMenuActions.Output> {

    record Input(List<SingleMenuGrant> singleMenuGrantList) {

        public record SingleMenuGrant(String menuName,
                                      List<ActionCode> actionList) { }

    }

    record Output(boolean granted) { }

}
