package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetAllParticipantUser;
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

    @GetMapping(value = "/secured/getAllParticipantUsers")
    public ResponseEntity<Response> execute(@RequestParam("participantId") String participantId)
        throws DomainException, JsonProcessingException {

        GetAllParticipantUser.Output output = this.getAllParticipantUser.execute(
            new GetAllParticipantUser.Input(new ParticipantId(Long.parseLong(participantId))));

        List<Response.UserInfo> userInfoList = new ArrayList<>();

        for (var participantUser : output.userInfoList()) {
            userInfoList.add(new Response.UserInfo(participantUser.participantUserId()
                                                                  .getId()
                                                                  .toString(),
                                                   participantUser.name(),
                                                   participantUser.email()
                                                                  .getValue(),
                                                   participantUser.firstName(),
                                                   participantUser.lastName(),
                                                   participantUser.jobTitle(),
                                                   participantUser.roleType(),
                                                   participantUser.status(),
                                                   participantUser.createdDate()
                                                                  .getEpochSecond()));
        }

        var response = new GetAllParticipantUserController.Response(userInfoList);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("userInfoList") List<UserInfo> userInfoList) implements Serializable {

        @JsonIgnoreProperties(ignoreUnknown = true)
        public record UserInfo(@JsonProperty("participantUserId") String participantUserId,
                               @JsonProperty("name") String name,
                               @JsonProperty("email") String email,
                               @JsonProperty("firstName") String firstName,
                               @JsonProperty("lastName") String lastName,
                               @JsonProperty("jobTitle") String jobTitle,
                               @JsonProperty("userRoleType") String roleType,
                               @JsonProperty("status") String status,
                               @JsonProperty("createdDate") Long createdDate) implements Serializable { }

    }

}
