package com.thitsaworks.operation_portal.api.participant.controller.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.misc.exception.DFSPPortalException;
import com.thitsaworks.operation_portal.usecase.participant.GetAllParticipantUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;
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
public class GetAllParticipantUserController {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllParticipantUserController.class);

    @Autowired
    private GetAllParticipantUser getAllParticipantUser;

    @RequestMapping(value = "/secured/get_all_participant_users", method = RequestMethod.GET)
    public ResponseEntity<Response> execute(@RequestParam("participantId") String participantId)
            throws DFSPPortalException {

        GetAllParticipantUser.Output output = this.getAllParticipantUser.execute(
                new GetAllParticipantUser.Input(new ParticipantId(Long.parseLong(participantId))));

        List<Response.UserInfo> userInfoList = new ArrayList<>();
        for (var participantUser : output.userInfoList()) {
            userInfoList.add(new Response.UserInfo(participantUser.participantUserId().getId().toString(),
                    participantUser.name(), participantUser.email().getValue(), participantUser.firstName(),
                    participantUser.lastName(), participantUser.jobTitle(),participantUser.roleType() ,participantUser.status(),
                    participantUser.createdDate().getEpochSecond()));
        }

        return new ResponseEntity<>(new Response(userInfoList), HttpStatus.OK);
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Response implements Serializable {

        @JsonProperty("userinfo_list")
        private List<UserInfo> userInfoList;

        @Value
        public static class UserInfo implements Serializable {

            @JsonProperty("participant_user_id")
            private String participantUserId;

            @JsonProperty("name")
            private String name;

            @JsonProperty("email")
            private String email;

            @JsonProperty("first_name")
            private String firstName;

            @JsonProperty("last_name")
            private String lastName;

            @JsonProperty("job_title")
            private String jobTitle;

            @JsonProperty("user_role_type")
            private String roleType;

            @JsonProperty("status")
            private String status;

            @JsonProperty("created_date")
            private Long createdDate;

        }

    }

}
