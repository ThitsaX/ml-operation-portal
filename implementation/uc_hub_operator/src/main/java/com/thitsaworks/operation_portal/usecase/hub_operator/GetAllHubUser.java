package com.thitsaworks.operation_portal.usecase.hub_operator;

import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.component.usecase.AbstractAuditableUseCase;
import com.thitsaworks.operation_portal.hubuser.identity.HubUserId;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

public abstract class GetAllHubUser extends
        AbstractAuditableUseCase<GetAllHubUser.Input, GetAllHubUser.Output> {

    public record Input() {}

    public record Output(List<HubUserInfo> userInfoList) {
        public record HubUserInfo(
                HubUserId hubUserId,
                String name,
                Email email,
                String firstName,
                String lastName,
                String jobTitle,
                Instant createdDate
        ) implements Serializable {}
    }
}
