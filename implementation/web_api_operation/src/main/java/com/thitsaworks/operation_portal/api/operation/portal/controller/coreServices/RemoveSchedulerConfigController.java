package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.SchedulerConfigId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.RemoveSchedulerConfig;
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
public class RemoveSchedulerConfigController {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveSchedulerConfigController.class);

    private final RemoveSchedulerConfig removeSchedulerConfig;

    private final ObjectMapper objectMapper;

    @PostMapping("/secured/removeSchedulerConfig")
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request)
        throws DomainException, JsonProcessingException {

        LOG.info("Remove Scheduler Config Request : [{}]", this.objectMapper.writeValueAsString(request));

        RemoveSchedulerConfig.Output output = this.removeSchedulerConfig.execute(
            new RemoveSchedulerConfig.Input(new SchedulerConfigId(request.schedulerConfigId)));

        var response = new Response(output.deleted());

        LOG.info("Remove Scheduler Config Response : [{}]", this.objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(@NotNull @JsonProperty("schedulerConfigId") Long schedulerConfigId)
        implements Serializable { }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
        @JsonProperty("removed") boolean removed
    ) { }

}