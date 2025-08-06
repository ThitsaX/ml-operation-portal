package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.type.Email;
import com.thitsaworks.operation_portal.component.common.type.PrincipalStatus;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

public interface OnboardUser extends
                            UseCase<OnboardUser.Input, OnboardUser.Output> {

    record Input(String name,
                 Email email,
                 String password,
                 String firstName,
                 String lastName,
                 String jobTitle,
                 ParticipantId participantId,
                 PrincipalStatus activeStatus) { }

    record Output(boolean created) { }

}
