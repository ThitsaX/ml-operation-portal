package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.thitsaworks.operation_portal.component.common.type.ActionCode;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.RevokeMenuActionList;
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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class RevokeMenuActionListController {

    private static final Logger LOG = LoggerFactory.getLogger(RevokeMenuActionListController.class);

    private final RevokeMenuActionList revokeMenuActionList;

    @PostMapping("/secured/revokeMenuActionList")
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request)
        throws DomainException {

        LOG.info("Revoke Menu Action List Request : [{}]", request);

        List<ActionCode> actionCodeList = new ArrayList<>();

        for (var action : request.actionCodeList()) {
            actionCodeList.add(new ActionCode(action));
        }

        var output = this.revokeMenuActionList.execute(new RevokeMenuActionList.Input(request.menuName(),
                                                                                      actionCodeList));

        var response = new Response(output.revoked());

        LOG.info("Revoke Menu Action List Response : [{}]", response);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(@NotBlank String menuName,
                          List<String> actionCodeList) implements Serializable { }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(boolean revoked) implements Serializable { }

}
