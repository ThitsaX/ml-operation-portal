package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.CreateSchedulerConfig;
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

@RestController
@RequiredArgsConstructor
public class CreateSchedulerConfigController {

    private static final Logger LOG = LoggerFactory.getLogger(CreateSchedulerConfigController.class);

    private final CreateSchedulerConfig createSchedulerConfig;

    @PostMapping("/secured/schedulerConfig")
    public ResponseEntity<Response> execute(
        @Valid @RequestBody Request request) throws DomainException {

        CreateSchedulerConfig.Output output = this.createSchedulerConfig.execute(
            new CreateSchedulerConfig.Input(
                request.name(),
                request.cronExpression(),
                request.description()
            )
        );

        var response = new Response(output.created());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(
        @NotBlank @JsonProperty("name") String name,
        @NotBlank @JsonProperty("cronExpression") String cronExpression,
        @NotBlank @JsonProperty("description") String description
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
        @JsonProperty("created") boolean created
    ) {}
}
