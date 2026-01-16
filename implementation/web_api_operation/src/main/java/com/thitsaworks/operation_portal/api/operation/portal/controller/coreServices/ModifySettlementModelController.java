package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.SettlementModelId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.ModifySettlementModel;
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
public class ModifySettlementModelController {

    private static final Logger LOG = LoggerFactory.getLogger(ModifySettlementModelController.class);

    private final ModifySettlementModel modifySettlementModel;

    private final ObjectMapper objectMapper;

    @PostMapping("/secured/modifySettlementModel")
    public ResponseEntity<Response> execute(
        @Valid @RequestBody Request request) throws DomainException, JsonProcessingException {

        LOG.info("Modify Settlement Model Request : [{}]", this.objectMapper.writeValueAsString(request));

        ModifySettlementModel.Output output = this.modifySettlementModel.execute(
            new ModifySettlementModel.Input(new SettlementModelId(Long.parseLong(request.settlementModelId())),
                                            request.name(),
                                            request.modelType(),
                                            request.currencyId(),
                                            request.active(),
                                            request.autoCloseWindow(),
                                            request.manualCloseWindow(),
                                            request.zoneId()));

        var response = new Response(output.modified(),
                                    output.settlementModelId()
                                          .getEntityId()
                                          .toString());

        LOG.info("Modify Settlement Model Response : [{}]", this.objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(@NotNull @JsonProperty("settlementModelId") String settlementModelId,
                          @NotNull @JsonProperty("name") String name,
                          @JsonProperty("modelType") String modelType,
                          @JsonProperty("currencyId") String currencyId,
                          @NotNull @JsonProperty("active") boolean active,
                          @NotNull @JsonProperty("autoCloseWindow") boolean autoCloseWindow,
                          @NotNull @JsonProperty("manualCloseWindow") boolean manualCloseWindow,
                          @JsonProperty("zoneId") String zoneId
    ) implements Serializable {

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("isModified") boolean isCreated,
                           @JsonProperty("settlementModelId") String contactId) implements Serializable {
    }

}
