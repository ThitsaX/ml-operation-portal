package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.SchedulerConfigId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.scheduler.data.SchedulerConfigData;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetSchedulerConfigById;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GetSchedulerConfigByIdController {

    private static final Logger LOG = LoggerFactory.getLogger(GetSchedulerConfigByIdController.class);

    private final GetSchedulerConfigById getSchedulerConfigById;

    private final ObjectMapper objectMapper;

    @GetMapping("/secured/getSchedulerConfigById")
    public ResponseEntity<Response> execute(@Valid @RequestParam Long schedulerConfigId)
        throws DomainException, JsonProcessingException {

        LOG.info("Get Scheduler Config by Id Request : schedulerConfigId = [{}]", schedulerConfigId);

        GetSchedulerConfigById.Output output = this.getSchedulerConfigById.execute(
                new GetSchedulerConfigById.Input(new SchedulerConfigId(schedulerConfigId))
                                                                                  );

        var response = new Response(output.config());

        LOG.info("Get Scheduler Config by Id Response : [{}]", this.objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
        @JsonProperty("config") SchedulerConfigData config
    ) {

        public Response {

            if (config == null) {
                throw new IllegalArgumentException("Config cannot be null");
            }
        }

    }

}
