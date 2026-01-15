package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.SchedulerConfigId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.ModifySchedulerConfigStatus;
import jakarta.validation.Valid;
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
public class ModifySchedulerConfigStatusController {

    private static final Logger logger = LoggerFactory.getLogger(ModifySchedulerConfigStatusController.class);

    private final ModifySchedulerConfigStatus modifySchedulerConfigStatus;

    private final ObjectMapper objectMapper;

    @PostMapping("/secured/modifySchedulerConfigStatus")
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request)
        throws DomainException, JsonProcessingException {

        logger.info("Modify Scheduler Config Status Request : [{}]", this.objectMapper.writeValueAsString(request));

        var output = this.modifySchedulerConfigStatus.execute(new ModifySchedulerConfigStatus.Input(
            new SchedulerConfigId(Long.parseLong(request.schedulerConfigId())), request.active()));

        var response = new Response(output.modified(),
                                    output.schedulerConfigData()
                                          .toString());

        logger.info("Modify Scheduler Config Status Response : [{}]", this.objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(
        @JsonIgnoreProperties("schedulerConfigId") String schedulerConfigId,
        @JsonIgnoreProperties("active") boolean active
    ) {

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
        @JsonIgnoreProperties("modified") boolean modified,
        @JsonIgnoreProperties("schedulerConfigData") String schedulerConfigData
    ) {

    }

}

