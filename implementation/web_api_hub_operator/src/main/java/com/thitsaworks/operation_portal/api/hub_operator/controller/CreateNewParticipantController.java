package com.thitsaworks.operation_portal.api.hub_operator.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.DfspCode;
import com.thitsaworks.operation_portal.component.misc.exception.OperationPortalException;
import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.component.type.Mobile;
import com.thitsaworks.operation_portal.usecase.hub_operator.CreateNewParticipant;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class CreateNewParticipantController {

    private static final Logger LOG = LoggerFactory.getLogger(CreateNewParticipantController.class);

    private final CreateNewParticipant createNewParticipant;

    private final ObjectMapper objectMapper;

    @PostMapping(value = "/secured/create_new_participant")
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request)
            throws JsonProcessingException, OperationPortalException {

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
                                                                   contactInfo.contactType());
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
                                                                            liquidityProfile.isActive()));
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
            @NotNull @JsonProperty("dfsp_code") String dfspCode,
            @NotNull @JsonProperty("dfsp_name") String dfspName,
            @NotNull @JsonProperty("address") String address,
            @NotNull @JsonProperty("mobile") String mobile,
            @NotNull @JsonProperty("contact_info_list") List<ContactInfo> contactInfoList,
            @JsonProperty("liquidity_profile_list") List<LiquidityProfileInfo> liquidityProfileInfoList)
            implements Serializable {

        @JsonIgnoreProperties(ignoreUnknown = true)
        public record ContactInfo(
                @NotNull @JsonProperty("name") String name,
                @NotNull @JsonProperty("title") String title,
                @NotNull @JsonProperty("email") String email,
                @NotNull @JsonProperty("mobile") String mobile,
                @NotNull @JsonProperty("contact_type") String contactType) implements Serializable {
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public record LiquidityProfileInfo(
                @JsonProperty("liquidity_profile_id") String liquidityProfileId,
                @NotNull @JsonProperty("account_name") String accountName,
                @NotNull @JsonProperty("account_number") String accountNumber,
                @NotNull @JsonProperty("currency") String currency,
                @JsonProperty("is_active") Boolean isActive) implements Serializable {
        }

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
            @JsonProperty("participant_id") String participantId,
            @JsonProperty("created") boolean created) implements Serializable {
    }

}
