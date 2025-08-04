package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.thitsaworks.operation_portal.component.common.type.ActionCode;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.CreateAction;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CreateActionController {

    private static final Logger LOG = LoggerFactory.getLogger(CreateActionController.class);

    private final CreateAction createAction;

    @PostMapping("/secured/createAction")
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request)
        throws DomainException {

        var output = this.createAction.execute(new CreateAction.Input(new ActionCode(request.name()),
                                                                      request.description(),
                                                                      request.scope()));

        var response = new Response(output.actionId()
                                          .toString());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public record Request(@NotNull @NotBlank String name,
                          @NotNull @NotBlank String description,
                          @NotNull @NotBlank String scope) { }

    public record Response(String actionId) { }

}
