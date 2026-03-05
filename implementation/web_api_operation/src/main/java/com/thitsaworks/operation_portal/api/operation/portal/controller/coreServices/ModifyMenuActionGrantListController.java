package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.MenuId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.ModifyMenuGrantList;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ModifyMenuActionGrantListController {

    private static final Logger LOG = LoggerFactory.getLogger(
        ModifyMenuActionGrantListController.class);

    private final ModifyMenuGrantList modifyMenuGrantList;

    private final ObjectMapper objectMapper;

    @PostMapping("/secured/modifyMenuGrantList")
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request)
        throws DomainException, JsonProcessingException {

        LOG.info(
            "Modify Menu Grant List Request : [{}]", this.objectMapper.writeValueAsString(request));

        List<ActionId> actionIdList = new ArrayList<>();
        for (var actionId : request.actionIdList()) {

            actionIdList.add(new ActionId(Long.parseLong(actionId)));
        }

        var output = this.modifyMenuGrantList.execute(
            new ModifyMenuGrantList.Input(
                new MenuId(Long.parseLong(request.menuId())),
                actionIdList));

        var response = new Response(output.modified());

        LOG.info(
            "Modify Menu Grant List Response : [{}]",
            this.objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(@NotBlank @JsonProperty("menuId") String menuId,
                          @JsonProperty("actionIdList") List<String> actionIdList)
        implements Serializable { }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("modified") boolean modified) implements Serializable { }

}
