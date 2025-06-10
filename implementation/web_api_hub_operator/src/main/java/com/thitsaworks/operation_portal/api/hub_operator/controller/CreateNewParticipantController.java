package com.thitsaworks.operation_portal.api.hub_operator.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.operation_portal.component.exception.DFSPPortalException;
import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.component.type.Mobile;
import com.thitsaworks.operation_portal.participant.type.DfspCode;
import com.thitsaworks.operation_portal.usecase.hub_operator.CreateNewParticipant;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@RestController
public class CreateNewParticipantController {

    private static final Logger LOG = LoggerFactory.getLogger(CreateNewParticipantController.class);

    @Autowired
    private CreateNewParticipant createNewParticipant;

    @RequestMapping(value = "/secured/create_new_participant", method = RequestMethod.POST)
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request) throws DFSPPortalException {

        List<CreateNewParticipant.Input.ContactInfo> contactInfoList = new ArrayList<>();
        List<CreateNewParticipant.Input.LiquidityProfileInfo> liquidityProfileInfoList = new ArrayList<>();

        if (request.contactInfoList != null || !request.contactInfoList.isEmpty()) {

            for (var contactInfo : request.contactInfoList) {

                CreateNewParticipant.Input.ContactInfo contact =
                        new CreateNewParticipant.Input.ContactInfo(contactInfo.getName(), contactInfo.getTitle(),
                                new Email(contactInfo.getEmail()), new Mobile(contactInfo.getMobile()),
                                contactInfo.getContactType());
                contactInfoList.add(contact);
            }
        }

        //Liquidity Profile Info
        if (request.liquidityProfileInfoList != null || !request.liquidityProfileInfoList.isEmpty()) {

            for (var liquidityProfile : request.liquidityProfileInfoList) {

                liquidityProfileInfoList.add(
                        new CreateNewParticipant.Input.LiquidityProfileInfo(liquidityProfile.getAccountName(),
                                liquidityProfile.getAccountNumber(), liquidityProfile.getCurrency(),
                                liquidityProfile.getIsActive()));
            }
        }

        CreateNewParticipant.Output output = this.createNewParticipant.execute(
                new CreateNewParticipant.Input(request.name, new DfspCode(request.dfspCode), request.dfspName,
                        request.address, new Mobile(request.mobile), contactInfoList, liquidityProfileInfoList));

        return new ResponseEntity<>(new Response(output.getParticipantId().getId().toString(),
                        output.isCreated()), HttpStatus.OK);
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Request implements Serializable {

        @NonNull
        @JsonProperty("name")
        private String name;

        @NonNull
        @JsonProperty("dfsp_code")
        private String dfspCode;

        @NonNull
        @JsonProperty("dfsp_name")
        private String dfspName;

        @NonNull
        @JsonProperty("address")
        private String address;

        @NonNull
        @JsonProperty("mobile")
        private String mobile;

        @NonNull
        @JsonProperty("contact_info_list")
        private List<ContactInfo> contactInfoList;

        @JsonProperty("liquidity_profile_list")
        private List<LiquidityProfileInfo> liquidityProfileInfoList;

        @Getter
        @AllArgsConstructor
        @NoArgsConstructor
        public static class ContactInfo implements Serializable {

            @NonNull
            @JsonProperty("name")
            private String name;

            @NonNull
            @JsonProperty("title")
            private String title;

            @NonNull
            @JsonProperty("email")
            private String email;

            @NonNull
            @JsonProperty("mobile")
            private String mobile;

            @NonNull
            @JsonProperty("contact_type")
            private String contactType;

        }

        @Getter
        @AllArgsConstructor
        @NoArgsConstructor
        public static class LiquidityProfileInfo implements Serializable {

            @JsonProperty("liquidity_profile_id")
            private String liquidityProfileId;

            @NonNull
            @JsonProperty("account_name")
            private String accountName;

            @NonNull
            @JsonProperty("account_number")
            private String accountNumber;

            @NonNull
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
