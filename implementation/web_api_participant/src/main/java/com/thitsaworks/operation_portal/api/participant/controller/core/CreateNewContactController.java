package com.thitsaworks.operation_portal.api.participant.controller.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.type.ContactType;
import com.thitsaworks.operation_portal.component.misc.exception.OperationPortalException;
import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.component.type.Mobile;
import com.thitsaworks.operation_portal.usecase.common.CreateNewContact;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CreateNewContactController {

    private static final Logger LOG = LoggerFactory.getLogger(CreateNewContactController.class);

    private final CreateNewContact createNewContact;

    private final ObjectMapper objectMapper;

    @PostMapping("/secured/create_contact")
    public ResponseEntity<Response> execute(
            @Valid @RequestBody Request request) throws OperationPortalException, JsonProcessingException {

        LOG.info("Create new contact request : {}", this.objectMapper.writeValueAsString(request));

        List<CreateNewContact.Input.ContactInfo> contactInfoList = new ArrayList<>();

        for (Request.ContactInfo contactInfo : request.contactInfoList) {
            contactInfoList.add(new CreateNewContact.Input.ContactInfo(contactInfo.name(), contactInfo.title(),
                    new Email(contactInfo.email()), new Mobile(contactInfo.mobile()),
                                                                       ContactType.valueOf(contactInfo.contactType()
                                                                                                      .toUpperCase())));
        }

        CreateNewContact.Output output = this.createNewContact.execute(
                new CreateNewContact.Input(new ParticipantId(Long.parseLong(request.participantId)), contactInfoList));

        var response = new Response(request.participantId, output.created());

        LOG.info("Create new contact response : {}", this.objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(
            @NotNull
            @JsonProperty("participant_id")
            String participantId,

            @NotNull
            @JsonProperty("contact_info_list")
            List<ContactInfo> contactInfoList) {

        @JsonIgnoreProperties(ignoreUnknown = true)
        public record ContactInfo(
                @NotNull
                @JsonProperty("name")
                String name,

                @NotNull
                @JsonProperty("title")
                String title,

                @NotNull
                @JsonProperty("email")
                @Pattern(regexp = Email.FORMAT, message = "Email must be with valid format.")
                String email,

                @NotNull
                @JsonProperty("mobile")
                String mobile,

                @NotNull
                @JsonProperty("contact_type")
                String contactType) {
        }

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
            @JsonProperty("participant_id")
            String participantId,

            @JsonProperty("created")
            boolean created) {
    }

}
