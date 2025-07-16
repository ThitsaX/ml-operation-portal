package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.core_services.CreateGreetingMessage;
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

import java.io.Serializable;
import java.time.Instant;

@RestController
@RequiredArgsConstructor
public class CreateGreetingMessageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateGreetingMessageController.class);

    private final CreateGreetingMessage createGreetingMessage;

    @PostMapping(value = "/secured/createGreetingMessage")
    public ResponseEntity<Response> execute(
        @Valid @RequestBody Request request) throws DomainException {

        var input = new CreateGreetingMessage.Input(
            request.greetingTitle(),
            request.greetingDetail(),
            Instant.parse(request.greetingDate())
                         );
        var output = this.createGreetingMessage.execute(input);

        var response = new Response(output.created());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(
        @NotNull
        @JsonProperty("greetingTitle")
        String greetingTitle,

        @NotNull
        @JsonProperty("greetingDetail")
        String greetingDetail ,

        @NotNull @JsonProperty("greetingDate")
        String greetingDate
        ) implements Serializable {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
        @JsonProperty("isCreated")
        boolean isCreated) implements Serializable {





    }
}
