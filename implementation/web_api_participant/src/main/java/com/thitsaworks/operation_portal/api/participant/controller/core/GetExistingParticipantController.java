package com.thitsaworks.operation_portal.api.participant.controller.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.misc.exception.OperationPortalException;
import com.thitsaworks.operation_portal.usecase.common.GetExistingParticipant;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetExistingParticipantController {

    private static final Logger LOG = LoggerFactory.getLogger(GetExistingParticipantController.class);

    private final GetExistingParticipant getExistingParticipant;

    private final ObjectMapper objectMapper;

    @GetMapping("/secured/get_participant")
    public ResponseEntity<Response> execute(
            @RequestParam("participantId") String participantId) throws OperationPortalException, JsonProcessingException {

        LOG.info("Get participant request : participantId = {}", participantId);

        GetExistingParticipant.Output output = this.getExistingParticipant.execute(
                new GetExistingParticipant.Input(new ParticipantId(Long.parseLong(participantId))));

        List<Response.ContactInfo> contactInfoList = new ArrayList<>();

        for (var contact : output.contactInfoList()) {

            contactInfoList.add(new Response.ContactInfo(contact.contactId().getId().toString(),
                    contact.name(),
                    contact.title(),
                    contact.email().getValue(),
                    contact.mobile().getValue(),
                    contact.contactType()));
        }

        List<Response.LiquidityProfileInfo> liquidityProfileInfoList = new ArrayList<>();

        for (var liquidityProfile : output.liquidityProfileInfoList()) {

            liquidityProfileInfoList.add(
                    new Response.LiquidityProfileInfo(liquidityProfile.liquidityProfileId().getId().toString(),
                            liquidityProfile.accountName(),
                            liquidityProfile.accountNumber(),
                            liquidityProfile.currency(),
                            liquidityProfile.isActive()));
        }

        var response = new Response(output.participantId().getId().toString(),
                                                                     output.dfsp_code(),
                                                                     output.name(),
                                                                     output.address(),
                                                                     output.mobile().getValue(),
                                                                     output.createdDate().getEpochSecond(),
                                                                     contactInfoList,
                                                                     liquidityProfileInfoList);

        LOG.info("Get participant response : {}", this.objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
            @JsonProperty("participant_id") String participantId,
            @JsonProperty("dfsp_code") String dfsp_code,
            @JsonProperty("name") String name,
            @JsonProperty("address") String address,
            @JsonProperty("mobile") String mobile,
            @JsonProperty("created_date") Long createdDate,
            @JsonProperty("contact_info_list") List<ContactInfo> contactInfoList,
            @JsonProperty("liquidity_profile_list") List<LiquidityProfileInfo> liquidityProfileInfoList) {

        public record ContactInfo(
                @JsonProperty("contact_id") String contactId,
                @JsonProperty("name") String name,
                @JsonProperty("title") String title,
                @JsonProperty("email") String email,
                @JsonProperty("mobile") String mobile,
                @JsonProperty("contact_type") String contactType) {
        }

        public record LiquidityProfileInfo(
                @JsonProperty("liquidity_profile_id") String liquidityProfileId,
                @JsonProperty("account_name") String accountName,
                @JsonProperty("account_number") String accountNumber,
                @JsonProperty("currency") String currency,
                @JsonProperty("is_active") Boolean isActive) {
        }

    }
}
