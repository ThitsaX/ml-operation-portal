package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.type.Email;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;
import com.thitsaworks.operation_portal.core.iam.data.ActionData;
import com.thitsaworks.operation_portal.core.iam.data.MenuData;

import java.util.List;
import java.util.Map;

public interface GetUserProfile extends
                                UseCase<GetUserProfile.Input, GetUserProfile.Output> {

    record Input(UserId userId) {}

    record Output(
            UserId userId,
            String name,
            Email email,
            String firstName,
            String lastName,
            String jobTitle,
            ParticipantId participantId,
            Long createdDate,
            String participantName,
            String description,
            String logoFileType,
            byte[] logoBase64,
            List<String> roleList,
            Map<List<MenuData>, List<ActionData>> permittedMenuAndActionList
    ) {}

}
