package com.thitsaworks.operation_portal.core.audit.query;

import com.thitsaworks.operation_portal.core.audit.exception.UserNotFoundException;
import com.thitsaworks.component.common.identifier.RealmId;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

public interface GetAuditByParticipant {

    @Value
    class Input {

        private RealmId realmId;

        private Instant fromDate;

        private Instant toDate;

    }

    @Value
    class Output {

        private List<AuditInfo> auditInfoList;

        @Value
        public static class AuditInfo implements Serializable
        {

            private String userName;

            private String actionName;

            private Instant actionDate;

        }

    }

    Output execute(Input input) throws UserNotFoundException;

}
