package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.common.identifier.RoleId;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalRoleId;
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

@RestController
@RequiredArgsConstructor
public class AssignRoleToUserController {

    private static final Logger LOG = LoggerFactory.getLogger(AssignRoleToUserController.class);

    private final AssignRoleToUser assignRoleToUser;

    @PostMapping("/secured/assignRoleToUser")
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request) throws DomainException {

        var
            output =
            this.assignRoleToUser.execute(new AssignRoleToUser.Input(new PrincipalId(Long.parseLong(request.userId())),
                                                                     new RoleId(Long.parseLong(request.roleId()))));

        var response = new Response(output.principalRoleId());

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    public record Request(@NotNull @NotBlank String userId,
                          @NotNull @NotBlank String roleId) { }

    public record Response(PrincipalRoleId principalRoleId) { }

}
