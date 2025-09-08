package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.thitsaworks.operation_portal.component.common.type.ActionCode;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.GrantRoleActions;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GrantRoleActionsController {

    private static final Logger LOG = LoggerFactory.getLogger(GrantRoleActionsController.class);

    private final GrantRoleActions grantRoleActions;

    @PostMapping("/secured/grantRoleActions")
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request) throws DomainException {

        LOG.info("Grant role actions request: [{}]", request);

        List<GrantRoleActions.Input.SingleRoleGrant> singleRoleGrantList = new ArrayList<>();
        for (var singleRoleGrant : request.singleRoleGrantList()) {

            List<ActionCode> actionCodeList = new ArrayList<>();

            for (var action : singleRoleGrant.actionList()) {
                actionCodeList.add(new ActionCode(action));
            }

            singleRoleGrantList.add(new GrantRoleActions.Input.SingleRoleGrant(singleRoleGrant.role(), actionCodeList));

        }

        var output = this.grantRoleActions.execute(new GrantRoleActions.Input(singleRoleGrantList));

        var response = new Response(output.granted());

        LOG.info("Grant Role Actions Response: [{}]", response);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(List<SingleRoleGrant> singleRoleGrantList) implements Serializable {

        public record SingleRoleGrant(String role,
                                      List<String> actionList) implements Serializable { }

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(boolean granted) implements Serializable { }

}
