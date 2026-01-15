package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.api.operation.portal.security.UserContext;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
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

import java.io.Serializable;
import java.util.Base64;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetUserProfileController {

    private static final Logger LOG = LoggerFactory.getLogger(GetUserProfileController.class);

    private final GetUserProfile getUserProfile;

    private final ObjectMapper objectMapper;

    @GetMapping("/secured/getUserProfile")
    public ResponseEntity<Response> execute()
        throws DomainException, JsonProcessingException {

        UserContext
            userContext =
            (UserContext) SecurityContextHolder.getContext()
                                               .getAuthentication()
                                               .getDetails();

        GetUserProfile.Output output = this.getUserProfile.execute(
            new GetUserProfile.Input(new UserId(userContext.userId()
                                                           .getId())));

        List<Long> menuIds = output.permittedMenuAndActionList()
                                   .entrySet()
                                   .stream()
                                   .flatMap(entry -> entry.getKey()
                                                          .stream())
                                   .map(menu -> menu.menuId()
                                                    .getId())
                                   .distinct()
                                   .sorted()
                                   .toList();

        List<String> actionCodes = output.permittedMenuAndActionList()
                                         .entrySet()
                                         .stream()
                                         .flatMap(entry -> entry.getValue()
                                                                .stream())
                                         .map(action -> action.actionCode()
                                                              .getValue())
                                         .distinct()
                                         .sorted()
                                         .toList();

        var response = new Response(output.userId()
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
                                    output.logoFileType(),
                                    output.logoBase64() == null ? null : Base64.getEncoder()
                                                                               .encodeToString(output.logoBase64()),
                                    output.roleList(),
                                    output.participantId()
                                          .getId()
                                          .toString(),
                                    output.createdDate(),
                                    menuIds,
                                    actionCodes);

        LOG.info("Get User Profile Response : [{}]", this.objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
        @JsonProperty("userId") String userId,
        @JsonProperty("name") String name,
        @JsonProperty("email") String email,
        @JsonProperty("firstName") String firstName,
        @JsonProperty("lastName") String lastName,
        @JsonProperty("jobTitle") String jobTitle,
        @JsonProperty("participantName") String participantName,
        @JsonProperty("description") String description,
        @JsonProperty("logoFileType") String logoFileType,
        @JsonProperty("logo") String logoBase64,
        @JsonProperty("roleList") List<String> roleList,
        @JsonProperty("participantId") String participantId,
        @JsonProperty("createdDate") Long createdDate,
        @JsonProperty("accessMenuList") List<Long> menuList,
        @JsonProperty("accessActionList") List<String> actionList
    ) implements Serializable { }

}
