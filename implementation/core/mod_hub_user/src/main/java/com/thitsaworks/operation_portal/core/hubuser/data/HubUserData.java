package com.thitsaworks.operation_portal.core.hubuser.data;

import com.thitsaworks.operation_portal.component.common.identifier.HubUserId;
import com.thitsaworks.operation_portal.component.common.type.Email;
import com.thitsaworks.operation_portal.core.hubuser.model.HubUser;

import java.time.Instant;

public record HubUserData(HubUserId hubUserId,

                          String name,

                          Email email,

                          String firstName,

                          String lastName,

                          String jobTitle,

                          Instant createdDate) {

    public HubUserData(HubUser hubUser) {

        this(hubUser.getUserId(),

             hubUser.getName(),

             hubUser.getEmail(),

             hubUser.getFirstName(),

             hubUser.getLastName(),

             hubUser.getJobTitle(),

             hubUser.getCreatedAt());
    }

}
