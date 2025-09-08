package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.scheduler.data.SchedulerConfigData;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetAllSchedulerConfigs;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetAllSchedulerConfigsController {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllSchedulerConfigsController.class);

    private final GetAllSchedulerConfigs getAllSchedulerConfigs;

    @GetMapping("/secured/scheduler-configs")
    public ResponseEntity<Response> execute(
        @RequestParam(value = "active", required = false) Boolean active,
        @RequestParam(value = "sortBy", required = false) String sortBy,
        @RequestParam(value = "sortDirection", required = false) Sort.Direction sortDirection
    ) throws DomainException {
        LOG.debug("Fetching all scheduler configurations with active={}, sortBy={}, sortDirection={}", 
                 active, sortBy, sortDirection);

        GetAllSchedulerConfigs.Output output = this.getAllSchedulerConfigs.execute(
            new GetAllSchedulerConfigs.Input(
                active,
                sortBy,
                sortDirection
            )
        );

        var response = new Response(output.configs());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
        @JsonProperty("configs") List<@NotNull SchedulerConfigData> configs
    ) {
        public Response {
            if (configs == null) {
                throw new IllegalArgumentException("Configs list cannot be null");
            }
        }
    }
}
