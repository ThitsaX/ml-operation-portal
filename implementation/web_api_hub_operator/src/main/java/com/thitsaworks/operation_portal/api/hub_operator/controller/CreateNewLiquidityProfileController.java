package com.thitsaworks.operation_portal.api.hub_operator.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.common.CreateNewLiquidityProfile;
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
public class CreateNewLiquidityProfileController {

    private static final Logger LOG = LoggerFactory.getLogger(CreateNewLiquidityProfileController.class);

    private final CreateNewLiquidityProfile createNewLiquidityProfile;

    private final ObjectMapper objectMapper;

    @PostMapping(value = "/secured/create_liquidity_profile")
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request)
            throws JsonProcessingException, DomainException {

        LOG.info("Create liquidity profile request: {}", objectMapper.writeValueAsString(request));

        List<CreateNewLiquidityProfile.Input.LiquidityProfileInfo> liquidityProfileInfoList = new ArrayList<>();

        for (Request.LiquidityProfileInfo liquidityProfileInfo : request.liquidityProfileInfoList) {

            liquidityProfileInfoList.add(
                    new CreateNewLiquidityProfile.Input.LiquidityProfileInfo(liquidityProfileInfo.accountName,
                                                                             liquidityProfileInfo.accountNumber,
                                                                             liquidityProfileInfo.currency,
                                                                             liquidityProfileInfo.isActive));

        }

        CreateNewLiquidityProfile.Output output = this.createNewLiquidityProfile.execute(
                new CreateNewLiquidityProfile.Input(new ParticipantId(Long.parseLong(request.participantId)),
                                                    liquidityProfileInfoList));

        Response response = new Response(request.participantId, output.created());

        LOG.info("Create liquidity profile response: {}", objectMapper.writeValueAsString(response));

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
                Boolean isActive) implements Serializable {}

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
            @JsonProperty("participant_id")
            String participantId,

            @JsonProperty("created")
            boolean created) implements Serializable {}

}
