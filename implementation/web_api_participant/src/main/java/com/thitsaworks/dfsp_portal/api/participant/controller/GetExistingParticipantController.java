package com.thitsaworks.dfsp_portal.api.participant.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.dfsp_portal.component.exception.DFSPPortalException;
import com.thitsaworks.dfsp_portal.participant.identity.ParticipantId;
import com.thitsaworks.dfsp_portal.usecase.common.GetExistingParticipant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@RestController
public class GetExistingParticipantController {

    private static final Logger LOG = LoggerFactory.getLogger(GetExistingParticipantController.class);

    @Autowired
    private GetExistingParticipant getExistingParticipant;

    @RequestMapping(value = "/secured/get_participant", method = RequestMethod.GET)
    public ResponseEntity<Response> execute(@RequestParam("participantId") String participantId)
            throws DFSPPortalException {

        GetExistingParticipant.Output output = this.getExistingParticipant.execute(
                new GetExistingParticipant.Input(new ParticipantId(Long.parseLong(participantId))));

        List<Response.ContactInfo> contactInfoList = new ArrayList<>();

        for (var contact : output.getContactInfoList()) {

            contactInfoList.add(new Response.ContactInfo(contact.getContactId().getId().toString(),
                    contact.getName(),
                    contact.getTitle(),
                    contact.getEmail().getValue(),
                    contact.getMobile().getValue(),
                    contact.getContactType()));
        }

        List<Response.LiquidityProfileInfo> liquidityProfileInfoList = new ArrayList<>();

        for (var liquidityProfile : output.getLiquidityProfileInfoList()) {

            liquidityProfileInfoList.add(
                    new Response.LiquidityProfileInfo(liquidityProfile.getLiquidityProfileId().getId().toString(),
                            liquidityProfile.getAccountName(),
                            liquidityProfile.getAccountNumber(),
                            liquidityProfile.getCurrency(),
                            liquidityProfile.getIsActive()));
        }

        return new ResponseEntity<>(
                new Response(
                        output.getParticipantId().getId().toString(),
                        output.getDfsp_code(), output.getName(), output.getAddress(),
                        output.getMobile().getValue(),
                        output.getCreatedDate().getEpochSecond(),
                        contactInfoList, liquidityProfileInfoList)
                , HttpStatus.OK);
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Response implements Serializable {

        @JsonProperty("participant_id")
        protected String participantId;

        @JsonProperty("dfsp_code")
        protected String dfsp_code;

        @JsonProperty("name")
        protected String name;

        @JsonProperty("address")
        protected String address;

        @JsonProperty("mobile")
        protected String mobile;

        @JsonProperty("created_date")
        protected Long createdDate;

        @JsonProperty("contact_info_list")
        protected List<ContactInfo> contactInfoList;

        @JsonProperty("liquidity_profile_list")
        protected List<LiquidityProfileInfo> liquidityProfileInfoList;

        @Getter
        @AllArgsConstructor
        public static class ContactInfo implements Serializable {

            @JsonProperty("contact_id")
            protected String contactId;

            @JsonProperty("name")
            protected String name;

            @JsonProperty("title")
            protected String title;

            @JsonProperty("email")
            protected String email;

            @JsonProperty("mobile")
            protected String mobile;

            @JsonProperty("contact_type")
            protected String contactType;

        }

        @Getter
        @AllArgsConstructor
        public static class LiquidityProfileInfo implements Serializable {

            @JsonProperty("liquidity_profile_id")
            protected String liquidityProfileId;

            @JsonProperty("account_name")
            protected String accountName;

            @JsonProperty("account_number")
            protected String accountNumber;

            @JsonProperty("currency")
            protected String currency;

            @JsonProperty("is_active")
            protected Boolean isActive;

        }
    }
}
