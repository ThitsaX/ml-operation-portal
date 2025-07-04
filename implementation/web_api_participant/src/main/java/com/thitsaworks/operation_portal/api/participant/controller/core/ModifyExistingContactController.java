package com.thitsaworks.operation_portal.api.participant.controller.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.ContactId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.type.ContactType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.component.type.Mobile;
import com.thitsaworks.operation_portal.usecase.common.ModifyExistingContact;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
public class ModifyExistingContactController {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyExistingContactController.class);

    private final ModifyExistingContact modifyExistingContact;

    private final ObjectMapper objectMapper;

    @PostMapping("/secured/modify_contact")
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request)
            throws DomainException, JsonProcessingException {

        LOG.info("Modify existing contact request : {}", this.objectMapper.writeValueAsString(request));

        List<ModifyExistingContact.Input.ContactInfo> contactInfoList = new ArrayList<>();

        for (Request.ContactInfo contactInfo : request.contactInfoList) {

            contactInfoList.add(
                    new ModifyExistingContact.Input.ContactInfo(new ContactId(Long.parseLong(contactInfo.contactId)),
                            contactInfo.name(), contactInfo.title(), new Email(contactInfo.email()),
                                                                new Mobile(contactInfo.mobile()),
                                                                ContactType.valueOf(contactInfo.contactType.toUpperCase())));

        }

        ModifyExistingContact.Output output = this.modifyExistingContact.execute(
                new ModifyExistingContact.Input(new ParticipantId(Long.parseLong(request.participantId)),
                        contactInfoList));

        var response = new Response(request.participantId, output.modified());

        LOG.info("Modify existing contact response : {}", this.objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(
            @NotNull @JsonProperty("participant_id") String participantId,
            @NotNull @JsonProperty("contact_info_list") List<ContactInfo> contactInfoList) {

        @JsonIgnoreProperties(ignoreUnknown = true)
        public record ContactInfo(
                @JsonProperty("contact_id") String contactId,
                @NotNull @JsonProperty("name") String name,
                @NotNull @JsonProperty("title") String title,
                @NotNull @JsonProperty("email") String email,
                @NotNull @JsonProperty("mobile") String mobile,
                @NotNull @JsonProperty("contact_type") String contactType) {

        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
            @JsonProperty("participant_id") String participantId,
            @JsonProperty("modified") boolean modified) {

    }
}
