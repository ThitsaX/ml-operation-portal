package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.operation_portal.component.common.identifier.ContactId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.type.ContactType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.component.type.Mobile;
import com.thitsaworks.operation_portal.usecase.operation_portal.SaveContact;
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
public class SaveContactController {

    private static final Logger LOG = LoggerFactory.getLogger(SaveContactController.class);

    private final SaveContact saveContact;

    @PostMapping(value = "/secured/saveContact")
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request) throws DomainException {

        var id = request.contactId();
        var contactId = id == null || id.isBlank() ? null : new ContactId(Long.parseLong(id));

        var
            output =
            this.saveContact.execute(new SaveContact.Input(new ParticipantId(Long.parseLong(request.participantId())),
                                                           contactId,
                                                           request.name(),
                                                           request.title(),
                                                           new Email(request.email()),
                                                           new Mobile(request.mobile()),
                                                           ContactType.valueOf(request.contactType()
                                                                                      .toUpperCase())));

        var response = new Response(output.saved(),
                                    output.contactId()
                                          .getEntityId()
                                          .toString());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(@NotNull @JsonProperty("participantId") String participantId,
                          @JsonProperty("contactId") String contactId,
                          @NotNull @JsonProperty("name") String name,
                          @NotNull @JsonProperty("title") String title,
                          @NotNull @JsonProperty("email") @Pattern(
                              regexp = Email.FORMAT,
                              message = "Email must be with valid format.")
                          String email,
                          @NotNull @JsonProperty("mobile") String mobile,
                          @NotNull @JsonProperty("contactType") String contactType) { }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("isSaved") boolean isSaved,
                           @JsonProperty("contactId") String contactId) { }

}
