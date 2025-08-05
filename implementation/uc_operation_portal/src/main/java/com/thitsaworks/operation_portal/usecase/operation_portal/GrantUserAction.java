package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.util.List;

public interface GrantUserAction extends UseCase<GrantUserAction.Input,GrantUserAction.Output> {


    record Input(List<UserGrant> userGrantList) {
        public record UserGrant(PrincipalId principalId, ActionId actionId){}
    }

    record Output(boolean resultCode) { }


}
