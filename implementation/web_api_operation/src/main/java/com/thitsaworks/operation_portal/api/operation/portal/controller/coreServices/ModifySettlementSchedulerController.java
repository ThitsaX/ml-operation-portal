package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.operation_portal.component.common.identifier.SchedulerConfigId;
import com.thitsaworks.operation_portal.component.common.identifier.SettlementModelId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.ModifySettlementScheduler;
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
public class ModifySettlementSchedulerController {

    private static final Logger LOG = LoggerFactory.getLogger(ModifySettlementSchedulerController.class);

    private final ModifySettlementScheduler modifySettlementScheduler;

    @PostMapping("/secured/modifySettlementScheduler")
    public ResponseEntity<Response> execute(
            @Valid @RequestBody Request request
                                           ) throws DomainException {

        LOG.info("Updating Settlement Scheduler Request : schedulerConfigId: [{}]", request.schedulerConfigId());

        ModifySettlementScheduler.Output output = this.modifySettlementScheduler.execute(
                new ModifySettlementScheduler.Input(
                        new SettlementModelId(request.settlementModelId()),
                        new SchedulerConfigId(request.schedulerConfigId()),
                        request.name(),
                        request.description(),
                        request.cronExpression(),
                        request.active()
                ));

        var response = new Response(output.updated());

        LOG.info("Updating Settlement Scheduler Response : [{}]", response);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(
            @NotNull @JsonProperty("settlementModelId") Long settlementModelId,
            @NotNull @JsonProperty("schedulerConfigId") Long schedulerConfigId,
            @NotBlank @JsonProperty("name") String name,
            @NotBlank @JsonProperty("description") String description,
            @NotBlank @JsonProperty("cronExpression") String cronExpression,
            @NotNull @JsonProperty("active") Boolean active
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
            @JsonProperty("updated") boolean updated
    ) {}

}
