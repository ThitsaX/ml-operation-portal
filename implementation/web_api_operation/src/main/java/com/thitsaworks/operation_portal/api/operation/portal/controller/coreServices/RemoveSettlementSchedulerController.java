package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.SchedulerConfigId;
import com.thitsaworks.operation_portal.component.common.identifier.SettlementModelId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.RemoveSettlementScheduler;
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
public class RemoveSettlementSchedulerController {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveSettlementSchedulerController.class);

    private final RemoveSettlementScheduler removeSettlementScheduler;

    private final ObjectMapper objectMapper;

    @PostMapping("/secured/removeSettlementScheduler")
    public ResponseEntity<Response> execute(
        @Valid @RequestBody Request request) throws DomainException, JsonProcessingException {

        LOG.info("Remove Settlement Scheduler Request : [{}]", this.objectMapper.writeValueAsString(request));

        RemoveSettlementScheduler.Output output = this.removeSettlementScheduler.execute(
            new RemoveSettlementScheduler.Input(new SettlementModelId(Long.parseLong(request.settlementModelId())),
                                                new SchedulerConfigId(Long.parseLong(request.schedulerConfigId()))));

        var response = new Response(output.removed(),
                                    output.schedulerConfigId()
                                          .getEntityId()
                                          .toString());

        LOG.info("Remove Settlement Scheduler Response : [{}]", this.objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(@NotNull @JsonProperty("settlementModelId") String settlementModelId,
                          @NotNull @JsonProperty("schedulerConfigId") String schedulerConfigId
    ) implements Serializable {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("isRemoved") boolean isCreated,
                           @JsonProperty("schedulerConfigId") String schedulerConfigId) implements Serializable {
    }

}
