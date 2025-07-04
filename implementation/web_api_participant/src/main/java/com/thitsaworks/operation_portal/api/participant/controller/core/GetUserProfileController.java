package com.thitsaworks.operation_portal.api.participant.controller.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.api.participant.security.UserContext;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantUserId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.participant.GetUserProfile;
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

    private final ObjectMapper objectMapper;

    @GetMapping("/secured/get_user_profile")
    public ResponseEntity<Response> execute()
            throws DomainException, JsonProcessingException {

        LOG.info("Get user profile request : {}", "");

        UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication().getDetails();

        GetUserProfile.Output output = this.getUserProfile.execute(
                new GetUserProfile.Input(new ParticipantUserId(userContext.participantUserId().getId())));

        var response = new Response(output.participantUserId().getId().toString(),
                output.name(),
                output.email().getValue(),
                output.firstName(),
                output.lastName(),
                output.jobTitle(),
                output.dfspCode(),
                output.dfspName(),
                output.roleType(),
                output.participantId().getId().toString(),
                output.createdDate());

        LOG.info("Get user profile response : {}", this.objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
            @JsonProperty("user_id") String participantUserId,
            @JsonProperty("name") String name,
            @JsonProperty("email") String email,
            @JsonProperty("first_name") String firstName,
            @JsonProperty("last_name") String lastName,
            @JsonProperty("job_title") String jobTitle,
            @JsonProperty("dfsp_code") String dfspCode,
            @JsonProperty("dfsp_name") String dfspName,
            @JsonProperty("user_role_type") String roleType,
            @JsonProperty("participant_id") String participantId,
            @JsonProperty("created_date") Long createdDate
    ) {

    }

}
