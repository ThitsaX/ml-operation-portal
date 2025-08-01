package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.thitsaworks.operation_portal.component.common.type.iamtesttype.ActionCode;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.GrantMenuActions;
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
public class GrantMenuActionsController {

    private static final Logger LOG = LoggerFactory.getLogger(GrantMenuActionsController.class);

    private final GrantMenuActions grantMenuActions;

    @PostMapping("/secured/grantMenuActions")
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request) throws DomainException {

        List<GrantMenuActions.Input.SingleMenuGrant> singleMenuGrantList = new ArrayList<>();
        for (var singleMenuGrant : request.singleMenuGrantList()) {

            List<ActionCode> actionCodeList = new ArrayList<>();

            for (var action : singleMenuGrant.actionList()) {
                actionCodeList.add(new ActionCode(action));
            }

            singleMenuGrantList.add(new GrantMenuActions.Input.SingleMenuGrant(singleMenuGrant.menu(),
                                                                               actionCodeList));

        }

        var output = this.grantMenuActions.execute(new GrantMenuActions.Input(singleMenuGrantList));

        return new ResponseEntity<>(new Response(output.granted()), HttpStatus.OK);

    }

    public record Request(List<SingleMenuGrant> singleMenuGrantList) {

        public record SingleMenuGrant(String menu,
                                      List<String> actionList) { }

    }

    public record Response(boolean granted) { }

}
