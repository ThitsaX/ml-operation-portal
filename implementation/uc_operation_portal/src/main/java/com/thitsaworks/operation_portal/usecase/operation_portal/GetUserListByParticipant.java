package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.type.Email;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

public interface GetUserListByParticipant extends
                                       UseCase<GetUserListByParticipant.Input, GetUserListByParticipant.Output> {

     record Input(UserId userId) {}

     record Output(List<UserInfo> userInfoList) {}

     record UserInfo(
            UserId userId,
            String name,
            Email email,
            String firstName,
            String lastName,
            String jobTitle,
            List<String> roleList,
            String status,
            Instant createdDate
    ) implements Serializable {}

}
