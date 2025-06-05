package com.thitsaworks.dfsp_portal.api.participant.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.dfsp_portal.component.exception.DFSPPortalException;
import com.thitsaworks.dfsp_portal.component.type.Email;
import com.thitsaworks.dfsp_portal.component.type.Mobile;
import com.thitsaworks.dfsp_portal.participant.identity.ParticipantId;
import com.thitsaworks.dfsp_portal.usecase.common.CreateNewContact;
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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@RestController
public class CreateNewContactController {

    private static final Logger LOG = LoggerFactory.getLogger(CreateNewContactController.class);

    @Autowired
    private CreateNewContact createNewContact;

    @RequestMapping(value = "/secured/create_contact", method = RequestMethod.POST)
    public ResponseEntity<Response> execute(
            @Valid @RequestBody Request request) throws DFSPPortalException {

        List<CreateNewContact.Input.ContactInfo> contactInfoList = new ArrayList<>();

        for (Request.ContactInfo contactInfo : request.contactInfoList) {
            contactInfoList.add(new CreateNewContact.Input.ContactInfo(contactInfo.getName(), contactInfo.getTitle(),
                    new Email(contactInfo.getEmail()), new Mobile(contactInfo.getMobile()),
                    contactInfo.getContactType()));

        }

        CreateNewContact.Output output = this.createNewContact.execute(
                new CreateNewContact.Input(new ParticipantId(Long.parseLong(request.participantId)), contactInfoList));

        return new ResponseEntity<>(
                new Response(request.participantId, output.isCreated()), HttpStatus.OK);
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Request implements Serializable {

        @NotNull
        @JsonProperty("participant_id")
        private String participantId;

        @NotNull
        @JsonProperty("contact_info_list")
        private List<ContactInfo> contactInfoList;

        @Getter
        @AllArgsConstructor
        @NoArgsConstructor
        public static class ContactInfo implements Serializable {

            @NotNull
            @JsonProperty("name")
            private String name;

            @NotNull
            @JsonProperty("title")
            private String title;

            @NotNull
            @JsonProperty("email")
            @Pattern(regexp = Email.FORMAT, message = "Email must be with valid format.")
            private String email;

            @NotNull
            @JsonProperty("mobile")
            private String mobile;

            @NotNull
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

        @JsonProperty("created")
        private boolean created;

    }

}
