package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.common.identifier.RoleId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.RemoveRoleFromUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@RestController
@RequiredArgsConstructor
public class RemoveRoleFromUserController {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveRoleFromUserController.class);

    private final RemoveRoleFromUser removeRoleFromUser;

    private final ObjectMapper objectMapper;

    @PostMapping("/secured/removeRoleFromUser")
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request)
        throws DomainException, JsonProcessingException {

        LOG.info("Remove Role From User Request : [{}]", this.objectMapper.writeValueAsString(request));

        var
            output =
            this.removeRoleFromUser.execute(new RemoveRoleFromUser.Input(new PrincipalId(Long.parseLong(request.userId())),
                                                                         new RoleId(Long.parseLong(request.roleId()))));

        var response = new Response(output.removed());

        LOG.info("Remove Role From User Response : [{}]", this.objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(@NotNull @NotBlank @JsonProperty("userId") String userId,
                          @NotNull @NotBlank @JsonProperty("roleId") String roleId) implements Serializable { }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("removed") boolean removed) implements Serializable { }

}
