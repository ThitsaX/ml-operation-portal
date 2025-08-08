package com.thitsaworks.operation_portal.usecase.util;

import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.core.participant.query.UserQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Utility {

    private final UserQuery userQuery;

    public String getEmail(UserId userId) {

        return this.userQuery.find(userId)
                             .map(user -> user.email()
                                                 .getValue())
                             .orElse("unknown");
    }


}
