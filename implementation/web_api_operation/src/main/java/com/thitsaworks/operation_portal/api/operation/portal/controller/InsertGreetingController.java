package com.thitsaworks.operation_portal.api.operation.portal.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.hub_operator.InsertGreeting;
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

@RestController
@RequiredArgsConstructor
public class InsertGreetingController {

    private static final Logger LOGGER = LoggerFactory.getLogger(InsertGreetingController.class);

    private final InsertGreeting insertGreeting;

    @PostMapping(value = "/secured/insertGreeting")
    public ResponseEntity<Response> execute(
        @Valid @RequestBody Request request) throws DomainException {

        var input = new InsertGreeting.Input(
            request.greetingTitle(),
            request.greetingDetail()
        );
        var output = this.insertGreeting.execute(input);

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
        String greetingDetail) implements Serializable {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
        @JsonProperty("created")
        boolean created) implements Serializable {





    }
}
