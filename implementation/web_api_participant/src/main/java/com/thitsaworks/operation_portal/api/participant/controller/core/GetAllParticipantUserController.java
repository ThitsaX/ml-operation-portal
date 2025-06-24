package com.thitsaworks.operation_portal.api.participant.controller.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.misc.exception.OperationPortalException;
import com.thitsaworks.operation_portal.usecase.participant.GetAllParticipantUser;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetAllParticipantUserController {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllParticipantUserController.class);

    private final GetAllParticipantUser getAllParticipantUser;

    private final ObjectMapper objectMapper;

    @GetMapping(value = "/secured/get_all_participant_users")
    public ResponseEntity<Response> execute(@RequestParam("participantId") String participantId)
            throws OperationPortalException, JsonProcessingException {

        LOG.info("Get all participant user request : participantId = {}", participantId);

        GetAllParticipantUser.Output output = this.getAllParticipantUser.execute(
                new GetAllParticipantUser.Input(new ParticipantId(Long.parseLong(participantId))));

        List<Response.UserInfo> userInfoList = new ArrayList<>();

        for (var participantUser : output.userInfoList()) {
            userInfoList.add(new Response.UserInfo(participantUser.participantUserId().getId().toString(),
                    participantUser.name(), participantUser.email().getValue(), participantUser.firstName(),
                    participantUser.lastName(), participantUser.jobTitle(),participantUser.roleType() ,participantUser.status(),
                    participantUser.createdDate().getEpochSecond()));
        }

        var response = new GetAllParticipantUserController.Response(userInfoList);

        LOG.info("Get all participant user response : {}", this.objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("userinfo_list") List<UserInfo> userInfoList) implements Serializable {

        @JsonIgnoreProperties(ignoreUnknown = true)
        public record UserInfo(@JsonProperty("participant_user_id")
                               String participantUserId,

                               @JsonProperty("name")
                               String name,

                               @JsonProperty("email")
                               String email,

                               @JsonProperty("first_name")
                               String firstName,

                               @JsonProperty("last_name")
                               String lastName,

                               @JsonProperty("job_title")
                               String jobTitle,

                               @JsonProperty("user_role_type")
                               String roleType,

                               @JsonProperty("status")
                               String status,

                               @JsonProperty("created_date")
                               Long createdDate) implements Serializable {}

    }

}
