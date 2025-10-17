package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.type.ActionCode;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.util.List;

public interface GrantMenuActionList extends UseCase<GrantMenuActionList.Input, GrantMenuActionList.Output> {

    record Input(List<MenuGrant> menuGrantList) {

        public record MenuGrant(String menuName,
                                List<ActionCode> actionList) { }
    }

    record Output(boolean granted) { }

}
