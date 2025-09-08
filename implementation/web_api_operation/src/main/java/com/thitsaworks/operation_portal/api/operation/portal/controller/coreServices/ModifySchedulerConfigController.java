package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.ModifySchedulerConfig;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ModifySchedulerConfigController {

    private static final Logger LOG = LoggerFactory.getLogger(ModifySchedulerConfigController.class);

    private final ModifySchedulerConfig modifySchedulerConfig;

    @PutMapping("/secured/scheduler-configs/{configId}")
    public ResponseEntity<Response> execute(
        @PathVariable("configId") Long configId,
        @Valid @RequestBody Request request
    ) throws DomainException {
        LOG.debug("Updating scheduler configuration with id: {}", configId);

        ModifySchedulerConfig.Output output = this.modifySchedulerConfig.execute(
            new ModifySchedulerConfig.Input(
                request.name(),
                request.cronExpression(),
                request.description(),
                request.active()
            )
        );

        var response = new Response(output.updated());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(
        @NotBlank @JsonProperty("name") String name,
        @NotBlank @JsonProperty("cronExpression") String cronExpression,
        @NotBlank @JsonProperty("description") String description,
        @NotNull @JsonProperty("active") Boolean active
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
        @JsonProperty("isUpdated") boolean isUpdated
    ) {}
}
