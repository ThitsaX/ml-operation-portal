package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.identifier.RoleId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.ModifyUser;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ModifyUserController {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyUserController.class);

    private final ModifyUser modifyUser;

    @PostMapping("/secured/modifyUser")
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request)
        throws DomainException, JsonProcessingException {

        LOG.info("Modify User Request: [{}]", request);

        var output = this.modifyUser.execute(new ModifyUser.Input(new UserId(Long.parseLong(request.userId())),
                                                                  request.name(),
                                                                  request.firstName(),
                                                                  request.lastName(),
                                                                  new ParticipantId(Long.parseLong(
                                                                      request.participantId())),
                                                                  request.jobTitle(),
                                                                  request.roleIdList()
                                                                         .stream()
                                                                         .map(role -> new RoleId(Long.parseLong(role)))
                                                                         .toList()));

        var response = new Response(output.userId()
                                          .getId()
                                          .toString(), output.modified());

        LOG.info("Modify User Response: [{}]", response);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(@NotNull @JsonProperty("userId") String userId,
                          @NotNull @JsonProperty("name") String name,
                          @NotNull @JsonProperty("firstName") String firstName,
                          @NotNull @JsonProperty("lastName") String lastName,
                          @NotNull @JsonProperty("participantId") String participantId,
                          @NotNull @JsonProperty("jobTitle") String jobTitle,
                          @NotNull @JsonProperty("roleIdList") List<String> roleIdList
    ) implements Serializable { }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("userId") String userId,
                           @JsonProperty("modified") boolean modified) implements Serializable { }

}
