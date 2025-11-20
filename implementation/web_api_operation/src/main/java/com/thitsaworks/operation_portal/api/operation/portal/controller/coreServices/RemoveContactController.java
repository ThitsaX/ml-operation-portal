package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.operation_portal.component.common.identifier.ContactId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.RemoveContact;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

@RestController
@RequiredArgsConstructor
public class RemoveContactController {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveContactController.class);

    private final RemoveContact removeContact;

    @PostMapping(value = "/secured/removeContact")
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request) throws DomainException {

        LOG.info("Remove Contact Request : [{}]", request);

        var
            output =
            this.removeContact.execute(new RemoveContact.Input(new ParticipantId(Long.parseLong(request.participantId())),
                                                               new ContactId(Long.parseLong(request.contactId()))));

        var response = new Response(output.removed());

        LOG.info("Remove Contact Response : [{}]", response);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(@NotNull @NotBlank @JsonProperty("participantId") String participantId,
                          @NotNull @NotBlank @JsonProperty("contactId") String contactId) implements Serializable { }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("removed") boolean removed) implements Serializable { }

}
