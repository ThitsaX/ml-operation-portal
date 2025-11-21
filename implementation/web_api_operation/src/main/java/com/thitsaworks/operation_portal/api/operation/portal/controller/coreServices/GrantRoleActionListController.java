package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.thitsaworks.operation_portal.component.common.type.ActionCode;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.GrantRoleActionList;
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
public class GrantRoleActionListController {

    private static final Logger LOG = LoggerFactory.getLogger(GrantRoleActionListController.class);

    private final GrantRoleActionList grantRoleActionList;

    @PostMapping("/secured/grantRoleActionList")
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request) throws DomainException {

        LOG.info("Grant Role Action List Request : [{}]", request);

        List<GrantRoleActionList.Input.RoleGrant> roleGrantList = new ArrayList<>();
        for (var singleRoleGrant : request.roleGrantList()) {

            List<ActionCode> actionCodeList = new ArrayList<>();

            for (var action : singleRoleGrant.actionCodeList()) {
                actionCodeList.add(new ActionCode(action));
            }

            roleGrantList.add(new GrantRoleActionList.Input.RoleGrant(singleRoleGrant.roleName(),
                                                                      actionCodeList));

        }

        var output = this.grantRoleActionList.execute(new GrantRoleActionList.Input(roleGrantList));

        var response = new Response(output.granted());

        LOG.info("Grant Role Action List Response : [{}]", response);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(List<RoleGrant> roleGrantList) implements Serializable {

        public record RoleGrant(String roleName,
                                List<String> actionCodeList) implements Serializable { }

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(boolean granted) implements Serializable { }

}
