package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.scheduler.data.SchedulerConfigData;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetSchedulerConfigList;
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
public class GetSchedulerConfigListController {

    private static final Logger LOG = LoggerFactory.getLogger(GetSchedulerConfigListController.class);

    private final GetSchedulerConfigList getSchedulerConfigList;

    private final ObjectMapper objectMapper;

    @GetMapping("/secured/getSchedulerConfigList")
    public ResponseEntity<Response> execute(
        @RequestParam(
            value = "active",
            required = false) Boolean active,
        @RequestParam(
            value = "sortBy",
            required = false) String sortBy,
        @RequestParam(
            value = "sortDirection",
            required = false) Sort.Direction sortDirection) throws DomainException, JsonProcessingException {

        LOG.info("Get Scheduler Config List Request : active= [{}], sortBy= [{}], sortDirection= [{}]",
                 active, sortBy, sortDirection);

        GetSchedulerConfigList.Output output = this.getSchedulerConfigList.execute(
            new GetSchedulerConfigList.Input(
                active,
                sortBy,
                sortDirection
            ));

        var response = new Response(output.configs());

        LOG.info("Get Scheduler Config List Response :[{}]", this.objectMapper.writeValueAsString(response));

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
