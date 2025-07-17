package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetExistingParticipant;
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

    @GetMapping("/secured/getParticipant")
    public ResponseEntity<Response> execute(
        @RequestParam("participantId") String participantId) throws DomainException, JsonProcessingException {

        LOG.info("Get participant request : participantId = {}", participantId);

        GetExistingParticipant.Output output = this.getExistingParticipant.execute(
            new GetExistingParticipant.Input(new ParticipantId(Long.parseLong(participantId))));

        List<Response.ContactInfo> contactInfoList = new ArrayList<>();

        for (var contact : output.contactInfoList()) {

            contactInfoList.add(new Response.ContactInfo(contact.contactId()
                                                                .getId()
                                                                .toString(),
                                                         contact.name(),
                                                         contact.position(),
                                                         contact.email()
                                                                .getValue(),
                                                         contact.mobile()
                                                                .getValue(),
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
                                    output.dfspCode(),
                                    output.name(),
                                    output.address(),
                                    output.mobile()
                                          .getValue(),
                                    output.logo(),
                                    output.createdDate()
                                          .getEpochSecond(),
                                    contactInfoList,
                                    liquidityProfileInfoList);

        LOG.info("Get participant response : {}", this.objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
        @JsonProperty("participantId") String participantId,
        @JsonProperty("dfspCode") String dfspCode,
        @JsonProperty("name") String name,
        @JsonProperty("address") String address,
        @JsonProperty("mobile") String mobile,
        @JsonProperty("logo") byte[] logo,
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
