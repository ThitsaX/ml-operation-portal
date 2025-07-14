package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.operation_portal.component.common.identifier.GreetingId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.core_services.GetGreeting;
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
import retrofit2.http.POST;

import java.io.Serializable;

@RestController
@RequiredArgsConstructor
public class GetGreetingController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetGreetingController.class);
    private final GetGreeting getGreeting;

    @PostMapping(value = "/public/getGreeting")
    public ResponseEntity<Response> execute(
        @Valid @RequestBody Request request) throws DomainException {
        var input = new GetGreeting.Input(
            new GreetingId(Long.parseLong(request.greetingId()))
        );
        var output = this.getGreeting.execute(input);

        var response = new Response(
            output.greetingId().toString(),
            output.greetingTitle(),
            output.greetingDetail(),
            output.createdDate().getEpochSecond()
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(
        @NotNull
        @JsonProperty("greetingId")
        String greetingId) implements Serializable {
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(String greetingId,
                           String greetingTitle,
                           String greetingDetail,
                           long createdDate) implements Serializable {}


}
