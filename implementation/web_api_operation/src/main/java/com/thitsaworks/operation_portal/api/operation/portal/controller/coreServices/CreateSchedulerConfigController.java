package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.CreateSchedulerConfig;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneId;

@RestController
@RequiredArgsConstructor
public class CreateSchedulerConfigController {

    private static final Logger LOG = LoggerFactory.getLogger(CreateSchedulerConfigController.class);

    private final CreateSchedulerConfig createSchedulerConfig;

    private final ObjectMapper objectMapper;

    @PostMapping("/secured/createSchedulerConfig")
    public ResponseEntity<Response> execute(
        @Valid @RequestBody Request request) throws DomainException, JsonProcessingException {

        LOG.info("Create Scheduler Config Request : [{}]", this.objectMapper.writeValueAsString(request));

        CreateSchedulerConfig.Output output = this.createSchedulerConfig.execute(
            new CreateSchedulerConfig.Input(
                request.name(),
                request.jobName(),
                request.description(),
                request.cronExpression(),
                ZoneId.of(request.zoneId())
            )
                                                                                );

        var response = new Response(output.created());

        LOG.info("Create Scheduler Config Response : [{}]", this.objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(
        @NotBlank @JsonProperty("name") String name,
        @NotBlank @JsonProperty("jobName") String jobName,
        @NotBlank @JsonProperty("description") String description,
        @NotBlank @JsonProperty("cronExpression") String cronExpression,
        @NotBlank @JsonProperty("zoneId") String zoneId

    ) { }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
        @JsonProperty("created") boolean created
    ) { }

}
