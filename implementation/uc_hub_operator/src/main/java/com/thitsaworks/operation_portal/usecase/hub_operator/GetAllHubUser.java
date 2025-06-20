package com.thitsaworks.operation_portal.usecase.hub_operator;

import com.thitsaworks.operation_portal.component.common.identifier.HubUserId;
import com.thitsaworks.operation_portal.component.misc.usecase.AbstractAuditableUseCase;
import com.thitsaworks.operation_portal.component.type.Email;

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
