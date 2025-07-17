package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.ContactId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.type.ContactType;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;
import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.component.type.Mobile;

import java.io.Serializable;
import java.util.List;

public interface ModifyContact extends
                                       UseCase<ModifyContact.Input, ModifyContact.Output> {

    record Input(ParticipantId participantId,
                 List<Input.ContactInfo> contactInfoList) {

        public record ContactInfo(ContactId contactId,
                                  String name,
                                  String title,
                                  Email email,
                                  Mobile mobile,
                                  ContactType contactType) implements Serializable { }

    }

    record Output(boolean modified) { }

}
