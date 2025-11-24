package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.BlockUserActionList;
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
public class BlockUserActionListController {

    private static final Logger LOG = LoggerFactory.getLogger(BlockUserActionListController.class);

    private final BlockUserActionList blockUserActionList;

    @PostMapping("/secured/blockUserActionList")
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request) throws DomainException {

        LOG.info("Block User Action List Request : [{}]", request);

        var
            output =
            blockUserActionList.execute(new BlockUserActionList.Input(new PrincipalId(Long.parseLong(request.userId())),
                                                                      request.actionIdList()
                                                                       .stream()
                                                                       .map(id -> new ActionId(Long.parseLong(
                                                                           id)))
                                                                       .toList()));

        var response = new Response(output.blocked());

        LOG.info("Block User Action List Response : [{}]", response);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(@NotNull @NotEmpty @JsonProperty("userId") String userId,
                          @JsonProperty("actionIdList") List<String> actionIdList) {}

    public record Response(@JsonProperty("blocked") boolean blocked) {}

}

