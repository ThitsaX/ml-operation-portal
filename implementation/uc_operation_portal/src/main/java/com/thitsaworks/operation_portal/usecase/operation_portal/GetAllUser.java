package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.HubUserId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;
import com.thitsaworks.operation_portal.component.type.Email;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

public interface GetAllUser extends
                               UseCase<GetAllUser.Input, GetAllUser.Output> {

    record Input() { }

    record Output(List<HubUserInfo> userInfoList) {

        public record HubUserInfo(
            HubUserId hubUserId,
            String name,
            Email email,
            String firstName,
            String lastName,
            String jobTitle,
            Instant createdDate
        ) implements Serializable { }

    }

}
