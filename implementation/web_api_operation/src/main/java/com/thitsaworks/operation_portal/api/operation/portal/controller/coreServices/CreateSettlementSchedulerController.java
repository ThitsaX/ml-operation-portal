package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.thitsaworks.operation_portal.component.common.identifier.SettlementModelId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.CreateSettlementScheduler;
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
public class CreateSettlementSchedulerController {

    private static final Logger LOG = LoggerFactory.getLogger(CreateSettlementSchedulerController.class);

    private final CreateSettlementScheduler createSettlementScheduler;

    @PostMapping("/secured/createSettlementScheduler")
    public ResponseEntity<Response> execute(
            @Valid @RequestBody Request request) throws DomainException, JsonProcessingException {

        LOG.info("Create New Settlement Scheduler Request: [{}]", request);

        CreateSettlementScheduler.Output output = this.createSettlementScheduler.execute(
                new CreateSettlementScheduler.Input(new SettlementModelId(Long.parseLong(request.settlementModelId())),
                                                    request.name(),
                                                    request.description(),
                                                    request.cronExpression(),
                                                    request.zoneId()));

        var response = new Response(output.created(),
                                    output.schedulerConfigId()
                                          .getEntityId()
                                          .toString());

        LOG.info("Create New Settlement Scheduler Response: [{}]", response);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(@NotNull @JsonProperty("settlementModelId") String settlementModelId,
                          @NotNull @JsonProperty("name") String name,
                          @NotNull @JsonProperty("description") String description,
                          @NotNull @JsonProperty("cronExpression") String cronExpression,
                          @NotNull @JsonProperty("zoneId") String zoneId
    ) implements Serializable {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("isCreated") boolean isCreated,
                           @JsonProperty("schedulerConfigId") String schedulerConfigId) implements Serializable {
    }

}
