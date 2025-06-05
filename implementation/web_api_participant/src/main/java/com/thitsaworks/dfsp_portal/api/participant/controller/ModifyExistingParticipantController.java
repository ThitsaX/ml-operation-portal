package com.thitsaworks.dfsp_portal.api.participant.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.dfsp_portal.api.participant.security.UserContext;
import com.thitsaworks.dfsp_portal.component.exception.DFSPPortalException;
import com.thitsaworks.dfsp_portal.component.type.Email;
import com.thitsaworks.dfsp_portal.component.type.Mobile;
import com.thitsaworks.dfsp_portal.participant.identity.ContactId;
import com.thitsaworks.dfsp_portal.participant.identity.LiquidityProfileId;
import com.thitsaworks.dfsp_portal.participant.identity.ParticipantId;
import com.thitsaworks.dfsp_portal.usecase.common.ModifyExistingParticipant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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
public class ModifyExistingParticipantController {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyExistingParticipantController.class);

    @Autowired
    private ModifyExistingParticipant modifyExistingParticipant;

    @RequestMapping(value = "/secured/modify_participant", method = RequestMethod.POST)
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request)
            throws DFSPPortalException {

        UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication().getDetails();

        List<ModifyExistingParticipant.Input.ContactInfo> contactInfoList = new ArrayList<>();
        List<ModifyExistingParticipant.Input.LiquidityProfileInfo> liquidityProfileInfoList = new ArrayList<>();

        //Contact Info
        if (request.contactInfoList != null || !request.contactInfoList.isEmpty()) {

            for (Request.ContactInfo contactInfo : request.contactInfoList) {

                contactInfoList.add(new ModifyExistingParticipant.Input.ContactInfo(
                        contactInfo.contactId == null || contactInfo.contactId.isEmpty() ? null :
                                new ContactId(Long.parseLong(contactInfo.contactId)), contactInfo.getName(),
                        contactInfo.getTitle(), new Email(contactInfo.getEmail()), new Mobile(contactInfo.getMobile()),
                        contactInfo.getContactType()));
            }
        }

        //Liquidity Profile Info
        if (request.liquidityProfileInfoList != null || !request.liquidityProfileInfoList.isEmpty()) {

            for (var liquidityProfile : request.liquidityProfileInfoList) {

                liquidityProfileInfoList.add(new ModifyExistingParticipant.Input.LiquidityProfileInfo(
                        liquidityProfile.liquidityProfileId == null || liquidityProfile.liquidityProfileId.isEmpty() ?
                                null : new LiquidityProfileId(Long.parseLong(liquidityProfile.liquidityProfileId)),
                        liquidityProfile.getAccountName(), liquidityProfile.getAccountNumber(),
                        liquidityProfile.getCurrency(), liquidityProfile.getIsActive()));
            }
        }

        ModifyExistingParticipant.Output output = this.modifyExistingParticipant.execute(
                new ModifyExistingParticipant.Input(new ParticipantId(Long.parseLong(request.participantId)),
                        request.companyName, request.address, new Mobile(request.mobile), contactInfoList,
                        liquidityProfileInfoList, userContext.getAccessKey()));

        return new ResponseEntity<>(new Response(output.getParticipantId().getId().toString(), output.isModified()),
                HttpStatus.OK);
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
        @JsonProperty("name")
        private String companyName;

        @NotNull
        @JsonProperty("address")
        private String address;

        @NotNull
        @JsonProperty("mobile")
        private String mobile;

        @JsonProperty("contact_info_list")
        private List<ContactInfo> contactInfoList;

        @JsonProperty("liquidity_profile_list")
        private List<LiquidityProfileInfo> liquidityProfileInfoList;

        @Getter
        @AllArgsConstructor
        @NoArgsConstructor
        public static class ContactInfo implements Serializable {

            @JsonProperty("contact_id")
            private String contactId;

            @NotNull
            @JsonProperty("name")
            private String name;

            @NotNull
            @JsonProperty("title")
            private String title;

            @NotNull
            @JsonProperty("email")
            private String email;

            @NotNull
            @JsonProperty("mobile")
            private String mobile;

            @NotNull
            @JsonProperty("contact_type")
            private String contactType;

        }

        @Getter
        @AllArgsConstructor
        @NoArgsConstructor
        public static class LiquidityProfileInfo implements Serializable {

            @JsonProperty("liquidity_profile_id")
            private String liquidityProfileId;

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

        @JsonProperty("modified")
        private boolean modified;

    }
}
