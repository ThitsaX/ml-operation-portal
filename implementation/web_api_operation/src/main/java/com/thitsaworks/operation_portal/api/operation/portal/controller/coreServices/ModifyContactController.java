package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.thitsaworks.operation_portal.component.common.identifier.ContactId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.type.ContactType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.component.type.Mobile;
import com.thitsaworks.operation_portal.usecase.operation_portal.ModifyContact;
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
public class ModifyContactController {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyContactController.class);

    private final ModifyContact modifyContact;

    @PostMapping("/secured/modifyContact")
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request)
        throws DomainException, JsonProcessingException {

        List<ModifyContact.Input.ContactInfo> contactInfoList = new ArrayList<>();

        for (Request.ContactInfo contactInfo : request.contactInfoList) {

            contactInfoList.add(
                new ModifyContact.Input.ContactInfo(
                    contactInfo.contactId() == null ? null : new ContactId(Long.parseLong(contactInfo.contactId())),
                    contactInfo.name(), contactInfo.title(), new Email(contactInfo.email()),
                    new Mobile(contactInfo.mobile()),
                    ContactType.valueOf(contactInfo.contactType()
                                                   .toUpperCase())));

        }

        ModifyContact.Output output = this.modifyContact.execute(
            new ModifyContact.Input(new ParticipantId(Long.parseLong(request.participantId())),
                                    contactInfoList));

        var response = new Response(request.participantId(), output.modified());

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(
        @NotNull @JsonProperty("participantId") String participantId,
        @NotNull @JsonProperty("contactInfoList") List<ContactInfo> contactInfoList) {

        @JsonIgnoreProperties(ignoreUnknown = true)
        public record ContactInfo(
            @JsonProperty("contactId") String contactId,
            @NotNull @JsonProperty("name") String name,
            @NotNull @JsonProperty("title") String title,
            @NotNull @JsonProperty("email") String email,
            @NotNull @JsonProperty("mobile") String mobile,
            @NotNull @JsonProperty("contactType") String contactType) {

        }

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
        @JsonProperty("participantId") String participantId,
        @JsonProperty("modified") boolean modified) {

    }

}
