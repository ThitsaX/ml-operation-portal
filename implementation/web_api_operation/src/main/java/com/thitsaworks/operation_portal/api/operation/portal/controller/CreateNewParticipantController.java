package com.thitsaworks.operation_portal.api.operation.portal.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.ContactType;
import com.thitsaworks.operation_portal.component.common.type.DfspCode;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.component.type.Mobile;
import com.thitsaworks.operation_portal.usecase.hub_operator.CreateNewParticipant;
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
public class CreateNewParticipantController {

    private static final Logger LOG = LoggerFactory.getLogger(CreateNewParticipantController.class);

    private final CreateNewParticipant createNewParticipant;

    private final ObjectMapper objectMapper;

    @PostMapping(value = "/secured/createNewParticipant")
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request)
            throws JsonProcessingException, DomainException {

        LOG.info("Create new participant request: {}", objectMapper.writeValueAsString(request));

        List<CreateNewParticipant.Input.ContactInfo> contactInfoList = new ArrayList<>();
        List<CreateNewParticipant.Input.LiquidityProfileInfo> liquidityProfileInfoList = new ArrayList<>();

        if (request.contactInfoList() != null || !request.contactInfoList().isEmpty()) {

            for (var contactInfo : request.contactInfoList()) {

                CreateNewParticipant.Input.ContactInfo contact =
                        new CreateNewParticipant.Input.ContactInfo(contactInfo.name(),
                                                                   contactInfo.title(),
                                                                   new Email(contactInfo.email()),
                                                                   new Mobile(contactInfo.mobile()),
                                                                   ContactType.valueOf(contactInfo.contactType().toUpperCase()));
                contactInfoList.add(contact);
            }
        }

        //Liquidity Profile Info
        if (request.liquidityProfileInfoList() != null || !request.liquidityProfileInfoList().isEmpty()) {

            for (var liquidityProfile : request.liquidityProfileInfoList()) {

                liquidityProfileInfoList.add(
                        new CreateNewParticipant.Input.LiquidityProfileInfo(liquidityProfile.accountName(),
                                                                            liquidityProfile.accountNumber(),
                                                                            liquidityProfile.currency(),
                                                                            liquidityProfile.participantStatus()));
            }
        }

        CreateNewParticipant.Output output = this.createNewParticipant.execute(
                new CreateNewParticipant.Input(request.name(),
                                               new DfspCode(request.dfspCode()),
                                               request.dfspName(),
                                               request.address(),
                                               new Mobile(request.mobile()),
                                               contactInfoList,
                                               liquidityProfileInfoList));

        Response response = new Response(output.participantId().getId().toString(), output.created());

        LOG.info("Create new participant response: {}", objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(
            @NotNull @JsonProperty("name") String name,
            @NotNull @JsonProperty("dfspCode") String dfspCode,
            @NotNull @JsonProperty("dfspName") String dfspName,
            @NotNull @JsonProperty("address") String address,
            @NotNull @JsonProperty("mobile") String mobile,
            @NotNull @JsonProperty("contactInfoList") List<ContactInfo> contactInfoList,
            @JsonProperty("liquidityProfileList") List<LiquidityProfileInfo> liquidityProfileInfoList)
            implements Serializable {

        @JsonIgnoreProperties(ignoreUnknown = true)
        public record ContactInfo(
                @NotNull @JsonProperty("name") String name,
                @NotNull @JsonProperty("title") String title,
                @NotNull @JsonProperty("email") String email,
                @NotNull @JsonProperty("mobile") String mobile,
                @NotNull @JsonProperty("contactType") String contactType) implements Serializable {
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public record LiquidityProfileInfo(
                @JsonProperty("liquidityProfileId") String liquidityProfileId,
                @NotNull @JsonProperty("accountName") String accountName,
                @NotNull @JsonProperty("accountNumber") String accountNumber,
                @NotNull @JsonProperty("currency") String currency,
                @JsonProperty("participantStatus") Boolean participantStatus) implements Serializable {
        }

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
            @JsonProperty("participant_id") String participantId,
            @JsonProperty("created") boolean created) implements Serializable {
    }

}
