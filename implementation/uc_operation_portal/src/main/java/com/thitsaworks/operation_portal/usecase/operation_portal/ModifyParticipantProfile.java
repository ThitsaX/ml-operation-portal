package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;
import com.thitsaworks.operation_portal.component.type.Mobile;

public interface ModifyParticipantProfile extends
                                          UseCase<ModifyParticipantProfile.Input, ModifyParticipantProfile.Output> {

    record Input(ParticipantId participantId,
                 String companyName,
                 String address,
                 Mobile mobile,
                 byte[] logo,
                 AccessKey accessKey) { }

    record Output(boolean modified, ParticipantId participantId) { }

}
