package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.type.ContactType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.common.type.Email;
import com.thitsaworks.operation_portal.component.common.type.Mobile;
import com.thitsaworks.operation_portal.usecase.operation_portal.CreateNewContact;
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

@RestController
@RequiredArgsConstructor
public class CreateNewContactController {

    private static final Logger LOG = LoggerFactory.getLogger(CreateNewContactController.class);

    private final CreateNewContact createNewContact;

    @PostMapping("/secured/createContact")
    public ResponseEntity<Response> execute(
        @Valid @RequestBody Request request) throws DomainException, JsonProcessingException {

        CreateNewContact.Output output = this.createNewContact.execute(
            new CreateNewContact.Input(new ParticipantId(Long.parseLong(request.participantId())),
                                       request.name(),
                                       request.position(),
                                       new Email(request.email()),
                                       new Mobile(request.mobile()),
                                       ContactType.valueOf(request.contactType()
                                                                  .toUpperCase())));

        var response = new Response(output.created(),
                                    output.contactId()
                                          .getEntityId()
                                          .toString());

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(@NotNull @JsonProperty("participantId") String participantId,
                          @NotNull @JsonProperty("name") String name,
                          @NotNull @JsonProperty("position") String position,
                          @NotNull @JsonProperty("email") @Pattern(
                              regexp = Email.FORMAT,
                              message = "Email must be with valid format.") String email,
                          @NotNull @JsonProperty("mobile") String mobile,
                          @NotNull @JsonProperty("contactType") String contactType) {

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("isCreated") boolean isCreated,
                           @JsonProperty("contactId") String contactId) {
    }

}
