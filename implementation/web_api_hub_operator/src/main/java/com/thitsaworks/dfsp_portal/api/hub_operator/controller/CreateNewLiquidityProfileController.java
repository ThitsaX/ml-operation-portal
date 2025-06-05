package com.thitsaworks.dfsp_portal.api.hub_operator.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.dfsp_portal.component.exception.DFSPPortalException;
import com.thitsaworks.dfsp_portal.participant.identity.ParticipantId;
import com.thitsaworks.dfsp_portal.usecase.common.CreateNewLiquidityProfile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@RestController
public class CreateNewLiquidityProfileController {

    private static final Logger LOG = LoggerFactory.getLogger(CreateNewLiquidityProfileController.class);

    @Autowired
    private CreateNewLiquidityProfile createNewLiquidityProfile;

    @RequestMapping(value = "/secured/create_liquidity_profile", method = RequestMethod.POST)
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request) throws DFSPPortalException {

        List<CreateNewLiquidityProfile.Input.LiquidityProfileInfo> liquidityProfileInfoList = new ArrayList<>();

        for (Request.LiquidityProfileInfo liquidityProfileInfo : request.liquidityProfileInfoList) {

            liquidityProfileInfoList.add(
                    new CreateNewLiquidityProfile.Input.LiquidityProfileInfo(liquidityProfileInfo.accountName,
                            liquidityProfileInfo.accountNumber, liquidityProfileInfo.currency,
                            liquidityProfileInfo.isActive));

        }

        CreateNewLiquidityProfile.Output output = this.createNewLiquidityProfile.execute(
                new CreateNewLiquidityProfile.Input(new ParticipantId(Long.parseLong(request.participantId)),
                        liquidityProfileInfoList));

        return new ResponseEntity<>(new Response(request.participantId, output.isCreated()), HttpStatus.OK);
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Request implements Serializable {

        @NotNull
        @JsonProperty("participant_id")
        private String participantId;

        @NotNull
        @JsonProperty("liquidity_profile_list")
        private List<Request.LiquidityProfileInfo> liquidityProfileInfoList;

        @Getter
        @AllArgsConstructor
        @NoArgsConstructor
        public static class LiquidityProfileInfo implements Serializable {

            @NotNull
            @JsonProperty("account_name")
            private String accountName;

            @NotNull
            @JsonProperty("account_number")
            private String accountNumber;

            @NotNull
            @JsonProperty("currency")
            private String currency;

            @JsonProperty("is_active")
            private Boolean isActive;

        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Response implements Serializable {

        @JsonProperty("participant_id")
        private String participantId;

        @JsonProperty("created")
        private boolean created;

    }
}
