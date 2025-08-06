package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantUserId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;
import com.thitsaworks.operation_portal.component.common.type.Email;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

public interface GetUserList extends
                                       UseCase<GetUserList.Input, GetUserList.Output> {

     record Input(ParticipantId participantId) {}

     record Output(List<UserInfo> userInfoList) {}

     record UserInfo(
            ParticipantUserId participantUserId,
            String name,
            Email email,
            String firstName,
            String lastName,
            String jobTitle,
            String roleType,
            String status,
            Instant createdDate
    ) implements Serializable {}

}
