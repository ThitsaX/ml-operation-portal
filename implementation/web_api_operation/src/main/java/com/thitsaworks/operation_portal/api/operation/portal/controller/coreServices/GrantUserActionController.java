package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.GrantUserAction;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GrantUserActionController {

    private static final Logger LOG = LoggerFactory.getLogger(GrantUserActionController.class);

    private final GrantUserAction grantUserAction;

    @PostMapping("/secured/grantUserActions")
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request) throws DomainException {

        List<GrantUserAction.Input.UserGrant> userGrantList = new ArrayList<>();
        for (var userGrant : request.userGrantList()) {
            userGrantList.add(new GrantUserAction.Input.UserGrant(
                new PrincipalId(Long.parseLong(userGrant.principalId())),
                new ActionId(Long.parseLong(userGrant.actionId()))));
        }

        var output = this.grantUserAction.execute(new GrantUserAction.Input(userGrantList));

        return new ResponseEntity<>(new Response(output.resultCode()), HttpStatus.OK);
    }

    public record Request(List<UserGrant> userGrantList) {

        public record UserGrant(String principalId, String actionId) { }

    }

    public record Response(boolean resultCode) { }

}
