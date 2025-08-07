package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.operation_portal.component.common.identifier.GreetingId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.UpdateGreeting;
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
public class UpdateGreetingController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateGreetingController.class);

    private final UpdateGreeting updateGreeting;

    @PostMapping(value = "/secured/updateGreeting")
    public ResponseEntity<Response> execute(
        @Valid @RequestBody Request request) throws DomainException {

        LOGGER.info("Update Greeting Request: [{}]", request);

        var input = new UpdateGreeting.Input(new GreetingId(Long.parseLong(request.greetingId())),
                                             request.greetingTitle(),
                                             request.greetingDetail(),
                                             request.isDeleted(),
                                             Instant.parse(request.greetingDate()));

        var output = this.updateGreeting.execute(input);

        var response = new Response(output.greetingId()
                                          .toString());

        LOGGER.info("Update Greeting Response: [{}]", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(@NotNull @JsonProperty("greetingId") String greetingId,
                          @NotNull @JsonProperty("greetingTitle") String greetingTitle,
                          @NotNull @JsonProperty("greetingDetail") String greetingDetail,
                          @NotNull @JsonProperty("isDeleted") boolean isDeleted,
                          @NotNull @JsonProperty("greetingDate") String greetingDate) implements Serializable { }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("greetingId") String greetingId) implements Serializable { }

}


