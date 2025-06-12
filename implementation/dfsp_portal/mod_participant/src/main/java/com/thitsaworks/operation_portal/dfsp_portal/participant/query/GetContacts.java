package com.thitsaworks.operation_portal.dfsp_portal.participant.query;

import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.component.type.Mobile;
import com.thitsaworks.operation_portal.dfsp_portal.participant.exception.ParticipantNotFoundException;
import com.thitsaworks.operation_portal.dfsp_portal.participant.identity.ContactId;
import com.thitsaworks.operation_portal.dfsp_portal.participant.identity.ParticipantId;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

public interface GetContacts {

    @Value
    class Input {

        private ParticipantId participantId;

    }

    @Value
    class Output {

        private List<ContactInfo> contactInfoList;

        @Value
        public static class ContactInfo implements Serializable {

            private ParticipantId participantId;

            private ContactId contactId;

            private String name;

            private String title;

            private Email email;

            private Mobile mobile;

            private String contactType;

        }

    }

    GetContacts.Output execute(GetContacts.Input input) throws ParticipantNotFoundException;

}
