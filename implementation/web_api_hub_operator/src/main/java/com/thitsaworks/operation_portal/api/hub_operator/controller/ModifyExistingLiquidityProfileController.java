package com.thitsaworks.operation_portal.api.hub_operator.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.LiquidityProfileId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.common.ModifyExistingLiquidityProfile;
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
import java.util.ArrayList;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class ModifyExistingLiquidityProfileController {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyExistingLiquidityProfileController.class);

    private final ModifyExistingLiquidityProfile modifyExistingLiquidityProfile;

    private final ObjectMapper objectMapper;

    @PostMapping(value = "/secured/modify_liquidity_profile")
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request)
            throws DomainException, JsonProcessingException, DomainException {

        LOG.info("Modify liquidity profile request: {}", objectMapper.writeValueAsString(request));

        List<ModifyExistingLiquidityProfile.Input.LiquidityProfileInfo> liquidityProfileInfoList = new ArrayList<>();

        for (Request.LiquidityProfileInfo liquidityProfileInfo : request.liquidityProfileInfoList) {

            liquidityProfileInfoList.add(new ModifyExistingLiquidityProfile.Input.LiquidityProfileInfo(
                    new LiquidityProfileId(Long.parseLong(liquidityProfileInfo.liquidityProfileId)),
                    liquidityProfileInfo.accountName(), liquidityProfileInfo.accountNumber(),
                    liquidityProfileInfo.currency(), liquidityProfileInfo.isActive()));

        }

        ModifyExistingLiquidityProfile.Output output = this.modifyExistingLiquidityProfile.execute(
                new ModifyExistingLiquidityProfile.Input(new ParticipantId(Long.parseLong(request.participantId)),
                        liquidityProfileInfoList));

        Response response = new Response(request.participantId, output.modified());

        LOG.info("Modify liquidity profile response: {}", objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(
            @NotNull
            @JsonProperty("participant_id")
            String participantId,

            @NotNull
            @JsonProperty("liquidity_profile_list")
            List<LiquidityProfileInfo> liquidityProfileInfoList) implements Serializable {

        @JsonIgnoreProperties(ignoreUnknown = true)
        public record LiquidityProfileInfo(
                @JsonProperty("liquidity_profile_id")
                String liquidityProfileId,

                @NotNull
                @JsonProperty("account_name")
                String accountName,

                @NotNull
                @JsonProperty("account_number")
                String accountNumber,

                @NotNull
                @JsonProperty("currency")
                String currency,

                @JsonProperty("is_active")
                Boolean isActive) implements Serializable {
        }

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
            @JsonProperty("participant_id")
            String participantId,

            @JsonProperty("modified")
            boolean modified) implements Serializable {
    }

}
