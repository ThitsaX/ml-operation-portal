package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetParticipant;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetParticipantController {

    private static final Logger LOG = LoggerFactory.getLogger(GetParticipantController.class);

    private final GetParticipant getParticipant;

    private final ObjectMapper objectMapper;

    @GetMapping("/secured/getParticipant")
    public ResponseEntity<Response> execute(
        @RequestParam("participantId") String participantId) throws DomainException, JsonProcessingException {

        LOG.info("Get Existing Participant Request : ParticipantId = [{}]", participantId);

        GetParticipant.Output output = this.getParticipant.execute(
            new GetParticipant.Input(new ParticipantId(Long.parseLong(participantId))));

        List<Response.ContactInfo> contactInfoList = new ArrayList<>();

        for (var contact : output.contactInfoList()) {

            contactInfoList.add(new Response.ContactInfo(contact.contactId()
                                                                .getId()
                                                                .toString(),
                                                         contact.name(),
                                                         contact.position(),
                                                         contact.email() != null ? contact.email()
                                                                                          .getValue() : null,
                                                         contact.mobile() != null ? contact.mobile()
                                                                                           .getValue() : null,
                                                         contact.contactType()));
        }

        List<Response.LiquidityProfileInfo> liquidityProfileInfoList = new ArrayList<>();

        for (var liquidityProfile : output.liquidityProfileInfoList()) {

            liquidityProfileInfoList.add(
                new Response.LiquidityProfileInfo(liquidityProfile.liquidityProfileId()
                                                                  .getId()
                                                                  .toString(),
                                                  liquidityProfile.bankName(),
                                                  liquidityProfile.accountName(),
                                                  liquidityProfile.accountNumber(),
                                                  liquidityProfile.currency(),
                                                  liquidityProfile.isActive()));
        }

        var response = new Response(output.participantId()
                                          .getId()
                                          .toString(),
                                    output.participantName(),
                                    output.description(),
                                    output.address(),
                                    output.mobile() != null ? output.mobile()
                                                                    .getValue() : null,
                                    output.logoType(),
                                    output.logo() == null ? null : Base64.getEncoder()
                                                                         .encodeToString(output.logo()),
                                    output.createdDate()
                                          .getEpochSecond(),
                                    contactInfoList,
                                    liquidityProfileInfoList);

        LOG.info("Get Existing Participant Response : [{}]", this.objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
        @JsonProperty("participantId") String participantId,
        @JsonProperty("participantName") String participantName,
        @JsonProperty("description") String description,
        @JsonProperty("address") String address,
        @JsonProperty("mobile") String mobile,
        @JsonProperty("logoType") String logoType,
        @JsonProperty("logo") String logo,
        @JsonProperty("createdDate") Long createdDate,
        @JsonProperty("contactInfoList") List<ContactInfo> contactInfoList,
        @JsonProperty("liquidityProfileInfoList") List<LiquidityProfileInfo> liquidityProfileInfoList) {

        public record ContactInfo(
            @JsonProperty("contactId") String contactId,
            @JsonProperty("name") String name,
            @JsonProperty("position") String position,
            @JsonProperty("email") String email,
            @JsonProperty("mobile") String mobile,
            @JsonProperty("contactType") String contactType) {
        }

        public record LiquidityProfileInfo(
            @JsonProperty("liquidityProfileId") String liquidityProfileId,
            @JsonProperty("bankName") String bankName,
            @JsonProperty("accountName") String accountName,
            @JsonProperty("accountNumber") String accountNumber,
            @JsonProperty("currency") String currency,
            @JsonProperty("isActive") Boolean isActive) {
        }

    }

}
