package com.thitsaworks.operation_portal.api.participant.controller.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.api.participant.security.UserContext;
import com.thitsaworks.operation_portal.component.common.identifier.ContactId;
import com.thitsaworks.operation_portal.component.common.identifier.LiquidityProfileId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.type.ContactType;
import com.thitsaworks.operation_portal.component.misc.exception.OperationPortalException;
import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.component.type.Mobile;
import com.thitsaworks.operation_portal.usecase.common.ModifyExistingParticipant;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ModifyExistingParticipantController {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyExistingParticipantController.class);

    private final ModifyExistingParticipant modifyExistingParticipant;

    private final ObjectMapper objectMapper;

    @PostMapping("/secured/modify_participant")
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request)
            throws OperationPortalException, JsonProcessingException {

        LOG.info("Modify participant request : {}", this.objectMapper.writeValueAsString(request));

        UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication().getDetails();

        List<ModifyExistingParticipant.Input.ContactInfo> contactInfoList = new ArrayList<>();

        List<ModifyExistingParticipant.Input.LiquidityProfileInfo> liquidityProfileInfoList = new ArrayList<>();

        if (request.contactInfoList() != null && !request.contactInfoList().isEmpty()) {
            for (ContactInfo contactInfo : request.contactInfoList()) {
                contactInfoList.add(new ModifyExistingParticipant.Input.ContactInfo(
                        contactInfo.contactId() == null || contactInfo.contactId().isEmpty() ? null :
                                new ContactId(Long.parseLong(contactInfo.contactId())), contactInfo.name(),
                        contactInfo.title(), new Email(contactInfo.email()), new Mobile(contactInfo.mobile()),
                        ContactType.valueOf(contactInfo.contactType())));
            }
        }

        if (request.liquidityProfileInfoList() != null && !request.liquidityProfileInfoList().isEmpty()) {
            for (LiquidityProfileInfo liquidityProfile : request.liquidityProfileInfoList()) {
                liquidityProfileInfoList.add(new ModifyExistingParticipant.Input.LiquidityProfileInfo(
                        liquidityProfile.liquidityProfileId() == null ||
                                liquidityProfile.liquidityProfileId().isEmpty() ?
                                null :
                                new LiquidityProfileId(
                                        Long.parseLong(liquidityProfile.liquidityProfileId())),
                        liquidityProfile.accountName(), liquidityProfile.accountNumber(),
                        liquidityProfile.currency(), liquidityProfile.isActive()));
            }
        }

        ModifyExistingParticipant.Output output = this.modifyExistingParticipant.execute(
                new ModifyExistingParticipant.Input(new ParticipantId(Long.parseLong(request.participantId())),
                        request.companyName(), request.address(), new Mobile(request.mobile()), contactInfoList,
                        liquidityProfileInfoList, userContext.accessKey()));

        var response = new Response(output.participantId().getId().toString(), output.modified());

        LOG.info("Modify existing participant response : {}", this.objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(
            @NotNull @JsonProperty("participant_id") String participantId,
            @NotNull @JsonProperty("company_name") String companyName,
            @NotNull @JsonProperty("address") String address,
            @NotNull @JsonProperty("mobile") String mobile,
            @JsonProperty("contact_info_list") List<ContactInfo> contactInfoList,
            @JsonProperty("liquidity_profile_info_list") List<LiquidityProfileInfo> liquidityProfileInfoList
    ) {

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ContactInfo(
            @JsonProperty("contact_id") String contactId,
            @NotNull @JsonProperty("name") String name,
            @NotNull @JsonProperty("title") String title,
            @NotNull @JsonProperty("email") String email,
            @NotNull @JsonProperty("mobile") String mobile,
            @NotNull @JsonProperty("contact_type") String contactType
    ) {

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record LiquidityProfileInfo(
            @JsonProperty("liquidity_profile_id") String liquidityProfileId,
            @NotNull @JsonProperty("account_name") String accountName,
            @NotNull @JsonProperty("account_number") String accountNumber,
            @NotNull @JsonProperty("currency") String currency,
            @NotNull @JsonProperty("is_active") Boolean isActive
    ) {

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
            @JsonProperty("participant_id") String participantId,
            @JsonProperty("modified") boolean modified
    ) {

    }

}
