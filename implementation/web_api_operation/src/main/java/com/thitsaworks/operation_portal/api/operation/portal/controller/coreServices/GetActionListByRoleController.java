package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.RoleId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.util.MaskPassword;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetActionListByRole;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetActionListByRoleController {

    private static final Logger LOG = LoggerFactory.getLogger(GetActionListByRoleController.class);

    private final GetActionListByRole getActionListByRole;

    private final ObjectMapper objectMapper;

    @GetMapping(value = "/secured/getActionListByRole")
    public ResponseEntity<Response> execute(@RequestParam("roleId") String roleId)
        throws DomainException, JsonProcessingException {

        LOG.info(
            "Get Action List By Role Request : [{}]", MaskPassword.maskPassword(
                this.objectMapper,
                this.objectMapper.writeValueAsString(roleId)));

        var output = this.getActionListByRole.execute(
            new GetActionListByRole.Input(new RoleId(Long.parseLong(roleId))));

        var response = new Response(output
                                        .actionOptionList()
                                        .stream()
                                        .sorted(Comparator.comparing(a -> a.actionName()))
                                        .map(a -> new Response.ActionOption(
                                            a.actionId(), a.actionName(), a.selected(),
                                            a.mandatory()))
                                        .toList());

        LOG.info(
            "Get Action List By Role Response : [{}]",
            this.objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    public record Response(List<ActionOption> actionOptionList) {

        public record ActionOption(@JsonProperty("actionId") ActionId actionId,
                                   @JsonProperty("actionName") String actionName,
                                   @JsonProperty("selected") boolean selected,
                                   @JsonProperty("mandatory") boolean mandatory) { }

    }

}
