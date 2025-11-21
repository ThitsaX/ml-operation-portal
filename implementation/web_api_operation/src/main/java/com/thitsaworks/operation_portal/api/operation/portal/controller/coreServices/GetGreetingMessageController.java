package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetGreeting;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

@RestController
@RequiredArgsConstructor
public class GetGreetingMessageController {

    private static final Logger LOG = LoggerFactory.getLogger(GetGreetingMessageController.class);

    private final GetGreeting getGreeting;

    @GetMapping(value = "/secured/getGreetingMessage")
    public ResponseEntity<Response> execute() throws DomainException {

        var output = this.getGreeting.execute(new GetGreeting.Input());

        var response = new Response(output.greetingId()
                                          .toString(),
                                    output.greetingTitle(),
                                    output.greetingDetail(),
                                    output.isDeleted());

        LOG.info("Get Greeting Message Response : [{}]", response);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@NotNull @JsonProperty("greetingId") String greetingId,
                           @NotNull @JsonProperty("greetingTitle") String greetingTitle,
                           @NotNull @JsonProperty("greetingDetail") String greetingDetail,
                           @NotNull @JsonProperty("isDeleted") boolean isDeleted
    ) implements Serializable { }

}
