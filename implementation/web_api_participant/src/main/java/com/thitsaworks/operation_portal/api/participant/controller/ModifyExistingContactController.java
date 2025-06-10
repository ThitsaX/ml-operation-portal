package com.thitsaworks.operation_portal.api.participant.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.operation_portal.component.exception.DFSPPortalException;
import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.component.type.Mobile;
import com.thitsaworks.operation_portal.participant.identity.ContactId;
import com.thitsaworks.operation_portal.participant.identity.ParticipantId;
import com.thitsaworks.operation_portal.usecase.common.ModifyExistingContact;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.NonNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ModifyExistingContactController {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyExistingContactController.class);

    @Autowired
    private ModifyExistingContact modifyExistingContact;

    @RequestMapping(value = "/secured/modify_contact", method = RequestMethod.POST)
    public ResponseEntity<Response> execute(
            @Valid @RequestBody Request request) throws DFSPPortalException {

        List<ModifyExistingContact.Input.ContactInfo> contactInfoList = new ArrayList<>();

        for (Request.ContactInfo contactInfo : request.contactInfoList) {

            contactInfoList.add(
                    new ModifyExistingContact.Input.ContactInfo(new ContactId(Long.parseLong(contactInfo.contactId)),
                            contactInfo.getName(), contactInfo.getTitle(), new Email(contactInfo.getEmail()),
                            new Mobile(contactInfo.getMobile())));

        }

        ModifyExistingContact.Output output = this.modifyExistingContact.execute(
                new ModifyExistingContact.Input(new ParticipantId(Long.parseLong(request.participantId)),
                        contactInfoList));

        return new ResponseEntity<>(new Response(request.participantId, output.isModified()), HttpStatus.OK);
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Request implements Serializable {

        @NonNull
        @JsonProperty("participant_id")
        private String participantId;

        @NonNull
        @JsonProperty("contact_info_list")
        private List<ContactInfo> contactInfoList;

        @Getter
        @AllArgsConstructor
        @NoArgsConstructor
        public static class ContactInfo implements Serializable {

            @JsonProperty("contact_id")
            private String contactId;

            @NonNull
            @JsonProperty("name")
            private String name;

            @NonNull
            @JsonProperty("title")
            private String title;

            @NonNull
            @JsonProperty("email")
            private String email;

            @NonNull
            @JsonProperty("mobile")
            private String mobile;

            @NonNull
            @JsonProperty("contact_type")
            private String contactType;

        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Response implements Serializable {

        @JsonProperty("participant_id")
        private String participantId;

        @JsonProperty("modified")
        private boolean modified;

    }
}
