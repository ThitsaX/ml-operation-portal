package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.common.identifier.RoleId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.AssignRoleToUser;
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
public class AssignRoleToUserController {

    private static final Logger LOG = LoggerFactory.getLogger(AssignRoleToUserController.class);

    private final AssignRoleToUser assignRoleToUser;

    @PostMapping("/secured/assignRoleToUser")
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request) throws DomainException {

        LOG.info("Assign Role To User Request : [{}]", request);

        var
            output =
            this.assignRoleToUser.execute(new AssignRoleToUser.Input(new PrincipalId(Long.parseLong(request.userId())),
                                                                     new RoleId(Long.parseLong(request.roleId()))));

        var response = new Response(output.principalRoleId()
                                          .getId()
                                          .toString());

        LOG.info("Assign Role To User Response : [{}]", response);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(@NotNull @NotBlank @JsonProperty("userId") String userId,
                          @NotNull @NotBlank @JsonProperty("roleId") String roleId) implements Serializable {}

    public record Response(@JsonProperty("principalRoleId") String principalRoleId) implements Serializable {}

}
