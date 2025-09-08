package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetUserList;
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
public class GetUserListController {

    private static final Logger LOG = LoggerFactory.getLogger(GetUserListController.class);

    private final GetUserList getUserList;

    @GetMapping(value = "/secured/getUserList")
    public ResponseEntity<Response> execute(@RequestParam("participantId") String participantId)
        throws DomainException, JsonProcessingException {

        LOG.info("Get User List Request: ParticipantId = [{}]", participantId);

        GetUserList.Output output = this.getUserList.execute(
                new GetUserList.Input(new ParticipantId(Long.parseLong(participantId))));

        List<Response.UserInfo> userInfoList = new ArrayList<>();

        for (var user : output.userInfoList()) {
            userInfoList.add(new Response.UserInfo(user.userId()
                                                                  .getId()
                                                                  .toString(),
                                                   user.name(),
                                                   user.email()
                                                                  .getValue(),
                                                   user.firstName(),
                                                   user.lastName(),
                                                   user.jobTitle(),
                                                   user.roleList(),
                                                   user.status(),
                                                   user.createdDate()
                                                                  .getEpochSecond()));
        }

        var response = new GetUserListController.Response(userInfoList);

        LOG.info("Get User List Response: [{}]", response);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("userInfoList") List<UserInfo> userInfoList) implements Serializable {

        @JsonIgnoreProperties(ignoreUnknown = true)
        public record UserInfo(@JsonProperty("userId") String userId,
                               @JsonProperty("name") String name,
                               @JsonProperty("email") String email,
                               @JsonProperty("firstName") String firstName,
                               @JsonProperty("lastName") String lastName,
                               @JsonProperty("jobTitle") String jobTitle,
                               @JsonProperty("roleList") List<String> roleList,
                               @JsonProperty("status") String status,
                               @JsonProperty("createdDate") Long createdDate) implements Serializable { }

    }

}
