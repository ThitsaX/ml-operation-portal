package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.api.operation.portal.security.UserContext;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantUserId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetUserProfile;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GetUserProfileController {

    private static final Logger LOG = LoggerFactory.getLogger(GetUserProfileController.class);

    private final GetUserProfile getUserProfile;

    @GetMapping("/secured/getUserProfile")
    public ResponseEntity<Response> execute()
        throws DomainException, JsonProcessingException {

        UserContext
            userContext =
            (UserContext) SecurityContextHolder.getContext()
                                               .getAuthentication()
                                               .getDetails();

        GetUserProfile.Output output = this.getUserProfile.execute(
            new GetUserProfile.Input(new ParticipantUserId(userContext.userId()
                                                                      .getId())));

        var response = new Response(output.participantUserId()
                                          .getId()
                                          .toString(),
                                    output.name(),
                                    output.email()
                                          .getValue(),
                                    output.firstName(),
                                    output.lastName(),
                                    output.jobTitle(),
                                    output.participantName(),
                                    output.description(),
                                    output.roleType(),
                                    output.participantId()
                                          .getId()
                                          .toString(),
                                    output.createdDate());

        LOG.info("Get User Profile Response: [{}]", response);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
        @JsonProperty("participantUserId") String participantUserId,
        @JsonProperty("name") String name,
        @JsonProperty("email") String email,
        @JsonProperty("firstName") String firstName,
        @JsonProperty("lastName") String lastName,
        @JsonProperty("jobTitle") String jobTitle,
        @JsonProperty("participantName") String participantName,
        @JsonProperty("description") String description,
        @JsonProperty("userRoleType") String roleType,
        @JsonProperty("participantId") String participantId,
        @JsonProperty("createdDate") Long createdDate
    ) {

    }

}
