package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.RemoveSchedulerConfig;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RemoveSchedulerConfigController {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveSchedulerConfigController.class);

    private final RemoveSchedulerConfig removeSchedulerConfig;

    @DeleteMapping("/secured/schedulerConfig")
    public ResponseEntity<Response> execute(@Valid @RequestParam Long configId) throws DomainException {
        LOG.info("Received request to delete scheduler config with ID: {}", configId);

        RemoveSchedulerConfig.Output output = this.removeSchedulerConfig.execute(
            new RemoveSchedulerConfig.Input(configId)
                                                                                );

        var response = new Response(output.deleted());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
        @JsonProperty("removed") boolean removed
    ) {}
}