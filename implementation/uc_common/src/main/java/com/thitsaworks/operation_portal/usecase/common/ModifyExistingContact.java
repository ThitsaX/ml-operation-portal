package com.thitsaworks.operation_portal.usecase.common;

import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.component.type.Mobile;
import com.thitsaworks.operation_portal.component.usecase.AbstractAuditableUseCase;
import com.thitsaworks.operation_portal.participant.identity.ContactId;
import com.thitsaworks.operation_portal.participant.identity.ParticipantId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

public abstract class ModifyExistingContact extends
        AbstractAuditableUseCase<ModifyExistingContact.Input, ModifyExistingContact.Output> {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Input {

        private ParticipantId participantId;

        List<Input.ContactInfo> contactInfoList;

        @Getter
        @AllArgsConstructor
        @NoArgsConstructor
        public static class ContactInfo implements Serializable {

            private ContactId contactId;

            private String name;

            private String title;

            private Email email;

            private Mobile mobile;

        }

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Output {

        private boolean modified;

    }

}
