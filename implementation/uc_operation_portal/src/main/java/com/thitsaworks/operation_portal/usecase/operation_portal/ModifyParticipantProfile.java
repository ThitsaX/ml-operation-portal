package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;
import com.thitsaworks.operation_portal.component.common.type.Mobile;

public interface ModifyParticipantProfile extends
                                          UseCase<ModifyParticipantProfile.Input, ModifyParticipantProfile.Output> {

    record Input(ParticipantId participantId,
                 String description,
                 String address,
                 Mobile mobile,
                 String logoDataType,
                 byte[] logo) { }

    record Output(boolean modified, ParticipantId participantId) { }

}
