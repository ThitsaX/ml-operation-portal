package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.GrantUserActionList;
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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GrantUserActionListController {

    private static final Logger LOG = LoggerFactory.getLogger(GrantUserActionListController.class);

    private final GrantUserActionList grantUserActionList;

    @PostMapping("/secured/grantUserActionList")
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request) throws DomainException {

        LOG.info("Grant User Action List Request : [{}]", request);

        List<ActionId> actionIdList = new ArrayList<>();
        request.actionIdList()
               .forEach(actionId -> actionIdList.add(new ActionId(Long.parseLong(actionId))));

        var
            output =
            this.grantUserActionList.execute(new GrantUserActionList.Input(new PrincipalId(Long.parseLong(request.userId())),
                                                                           actionIdList));

        var response = new Response(output.resultCode());

        LOG.info("Grant User Action List Response : [{}]", response);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(@NotNull @NotBlank @JsonProperty("userId") String userId,
                          @JsonProperty("actionIdList") List<String> actionIdList) implements Serializable {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("granted") boolean granted) implements Serializable {}

}
