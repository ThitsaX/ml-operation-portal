package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.common.identifier.RoleId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.RemoveRoleFromUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @PostMapping("/secured/removeRoleFromUser")
    public Response execute(@Valid @RequestBody Request request) throws DomainException {

        LOG.info("Remove Role From User Request: [{}]", request);

        var
            output =
            this.removeRoleFromUser.execute(new RemoveRoleFromUser.Input(new PrincipalId(Long.parseLong(request.userId())),
                                                                         new RoleId(Long.parseLong(request.roleId()))));
        var response = new Response(output.removed());
        LOG.info("Remove Role From User Response: [{}]", response);
        return response;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(@NotNull @NotBlank String userId,
                          @NotNull @NotBlank String roleId) implements Serializable { }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(boolean removed) implements Serializable { }

}
