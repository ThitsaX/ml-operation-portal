package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.thitsaworks.operation_portal.component.common.type.ActionCode;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.RevokeRoleActionList;
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
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class RevokeRoleActionListController {

    private static final Logger LOG = LoggerFactory.getLogger(RevokeRoleActionListController.class);

    private final RevokeRoleActionList revokeRoleActionList;

    @PostMapping("/secured/revokeRoleActionList")
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request)
        throws DomainException {

        LOG.info("Revoke Role Action List Request : [{}]", request);

        List<ActionCode> actionCodeList = new ArrayList<>();

        for (var action : request.actionCodeList()) {
            actionCodeList.add(new ActionCode(action));
        }

        var output = this.revokeRoleActionList.execute(new RevokeRoleActionList.Input(request.roleName(),
                                                                                      actionCodeList));

        var response = new Response(output.revoked());

        LOG.info("Revoke Role Action List Response : [{}]", response);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(@NotNull @NotBlank String roleName,
                          List<String> actionCodeList) implements Serializable { }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(boolean revoked) implements Serializable { }

}
