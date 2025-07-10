package com.thitsaworks.operation_portal.usecase.core_services;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.type.ContactType;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;
import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.component.type.Mobile;

import java.io.Serializable;
import java.util.List;

public interface CreateNewContact extends UseCase<CreateNewContact.Input, CreateNewContact.Output> {

    record Input(ParticipantId participantId,
                 List<ContactInfo> contactInfoList) {

        public record ContactInfo(String name,
                                  String title,
                                  Email email,
                                  Mobile mobile,
                                  ContactType contactType) implements Serializable { }

    }

    record Output(boolean created) {

    }

}
