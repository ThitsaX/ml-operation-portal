package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.operation_portal.component.common.identifier.SchedulerConfigId;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneId;

@RestController
@RequiredArgsConstructor
public class ModifySchedulerConfigController {

    private static final Logger LOG = LoggerFactory.getLogger(ModifySchedulerConfigController.class);

    private final ModifySchedulerConfig modifySchedulerConfig;

    @PostMapping("/secured/modifySchedulerConfig")
    public ResponseEntity<Response> execute(
        @Valid @RequestBody Request request
    ) throws DomainException {

        LOG.debug("Updating scheduler configuration with schedulerConfigId: {}", request.schedulerConfigId());

        ModifySchedulerConfig.Output output = this.modifySchedulerConfig.execute(
            new ModifySchedulerConfig.Input(
                    new SchedulerConfigId(request.schedulerConfigId()),
                    request.name(),
                    request.jobName(),
                    request.description(),
                    request.cronExpression(),
                    ZoneId.of(request.zoneId()),
                    request.active()
            )
        );

        var response = new Response(output.updated());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(
            @NotNull @JsonProperty("schedulerConfigId") Long schedulerConfigId,
            @NotBlank @JsonProperty("name") String name,
            @NotBlank @JsonProperty("jobName") String jobName,
            @NotBlank @JsonProperty("description") String description,
            @NotBlank @JsonProperty("cronExpression") String cronExpression,
            @NotNull @JsonProperty("zoneId") String zoneId,
            @NotNull @JsonProperty("active") Boolean active
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
        @JsonProperty("updated") boolean updated
    ) {}
}
