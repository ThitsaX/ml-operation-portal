package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.thitsaworks.operation_portal.component.common.type.iamtesttype.ActionCode;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.test_iam.engine.IAMEngine;
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
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GrantRoleActionsController {

    private static final Logger LOG = LoggerFactory.getLogger(GrantRoleActionsController.class);

    private final GrantRoleActions grantRoleActions;

    @PostMapping("/secured/grantRoleActions")
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request) throws DomainException {

        List<GrantRoleActions.Input.SingleRoleGrant> singleRoleGrantList = new ArrayList<>();
        for (var singleRoleGrant : request.singleRoleGrantList()) {

            List<ActionCode> actionCodeList = new ArrayList<>();

            for (var action : singleRoleGrant.actions()) {
                actionCodeList.add(new ActionCode(action));
            }

            singleRoleGrantList.add(new GrantRoleActions.Input.SingleRoleGrant(singleRoleGrant.role(), actionCodeList));

        }

        var output = this.grantRoleActions.execute(new GrantRoleActions.Input(singleRoleGrantList));

        return new ResponseEntity<>(new Response(output.granted()), HttpStatus.OK);

    }

    public record Request(List<SingleRoleGrant> singleRoleGrantList) {

        public record SingleRoleGrant(String role,
                                      List<String> actions) { }

    }

    public record Response(boolean result) { }

}
