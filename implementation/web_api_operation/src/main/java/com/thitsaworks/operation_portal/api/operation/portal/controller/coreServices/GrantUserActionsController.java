package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.GrantUserActions;
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
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GrantUserActionsController {

    private static final Logger LOG = LoggerFactory.getLogger(GrantUserActionsController.class);

    private final GrantUserActions grantUserActions;

    @PostMapping("/secured/grantUserActions")
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request) throws DomainException {

        LOG.info("Grant User Actions Request : [{}]", request);

        List<ActionId> actionIdList = new ArrayList<>();
        request.actionIdList()
               .forEach(actionId -> actionIdList.add(new ActionId(Long.parseLong(actionId))));

        var
            output =
            this.grantUserActions.execute(new GrantUserActions.Input(new PrincipalId(Long.parseLong(request.userId())),
                                                                     actionIdList));

        var response = new Response(output.resultCode());

        LOG.info("Grant User Actions Response : [{}]", response);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public record Request(@NotNull @NotBlank String userId,
                          List<String> actionIdList) { }

    public record Response(boolean granted) { }

}
