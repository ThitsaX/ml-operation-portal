package com.thitsaworks.operation_portal.core.audit.command;

import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public interface CreateAudit {

    record Input(String actionName,
                 UserId actionBy,
                 RealmId realmId,
                 String inputInfo,
                 String outputInfo) {
    }

    record Output(boolean created) {
    }

    Output execute(Input input);

}
