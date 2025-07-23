package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.ContactId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.type.ContactType;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;
import com.thitsaworks.operation_portal.component.common.type.Email;
import com.thitsaworks.operation_portal.component.common.type.Mobile;

public interface ModifyContact extends
                               UseCase<ModifyContact.Input, ModifyContact.Output> {

    record Input(ParticipantId participantId,
                 ContactId contactId,
                 String name,
                 String position,
                 Email email,
                 Mobile mobile,
                 ContactType contactType) { }

    record Output(boolean modified) { }

}
