package com.thitsaworks.operation_portal.audit.domain.command;

import com.thitsaworks.operation_portal.audit.exception.UserNotFoundException;
import com.thitsaworks.operation_portal.audit.identity.UserId;
import com.thitsaworks.operation_portal.iam.identity.RealmId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public interface CreateAudit {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    class Input {

        private String actionName;

        private UserId actionBy;

        private RealmId realmId;

        private String inputInfo;

        private String outputInfo;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    class Output {

        private boolean created;

    }

    Output execute(Input input) throws UserNotFoundException;

}
