package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.BlockPrincipalActions;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BlockUserActionsController {

    private static final Logger LOG = LoggerFactory.getLogger(BlockUserActionsController.class);

    private final BlockPrincipalActions blockPrincipalActions;

    @PostMapping("/secured/blockUserActions")
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request) throws DomainException {


        var output = blockPrincipalActions.execute(new BlockPrincipalActions.Input(new PrincipalId(Long.parseLong(request.userId())),
                                                                                   request.actionIdList()
                                                                                .stream()
                                                                                .map(id -> new ActionId(Long.parseLong(
                                                                                    id)))
                                                                                .toList()));

        var response = new Response(output.blocked());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public record Request(@NotNull @NotEmpty String userId,
                          List<String> actionIdList) { }

    public record Response(boolean success) { }

}

