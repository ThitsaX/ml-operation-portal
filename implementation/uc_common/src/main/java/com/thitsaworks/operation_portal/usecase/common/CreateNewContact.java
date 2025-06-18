package com.thitsaworks.operation_portal.usecase.common;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.misc.usecase.AbstractAuditableUseCase;
import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.component.type.Mobile;

import java.io.Serializable;
import java.util.List;

public abstract class CreateNewContact extends
                                       AbstractAuditableUseCase<CreateNewContact.Input, CreateNewContact.Output> {

    public record Input(ParticipantId participantId,
                        List<ContactInfo> contactInfoList) {

        public record ContactInfo(String name,
                                  String title,
                                  Email email,
                                  Mobile mobile,
                                  String contactType) implements Serializable {

        }

    }

    public record Output(boolean created) {


    }

}
