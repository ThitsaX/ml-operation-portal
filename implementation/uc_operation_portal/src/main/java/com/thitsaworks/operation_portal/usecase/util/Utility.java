package com.thitsaworks.operation_portal.usecase.util;

import com.thitsaworks.operation_portal.component.common.identifier.HubUserId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.core.hubuser.query.HubUserQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Utility {


    private final HubUserQuery hubUserQuery;


    public String getEmail(HubUserId hubUserId) {

        return this.hubUserQuery.find(hubUserId)
                             .map(user -> user.email()
                                                 .getValue())
                             .orElse("unknown");
    }


}
