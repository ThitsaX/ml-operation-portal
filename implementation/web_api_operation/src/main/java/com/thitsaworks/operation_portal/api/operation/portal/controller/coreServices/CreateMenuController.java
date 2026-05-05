package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.MenuId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.CreateMenu;
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

import java.io.Serializable;

@RestController
@RequiredArgsConstructor
public class CreateMenuController {

    private static final Logger LOG = LoggerFactory.getLogger(CreateMenuController.class);

    private final CreateMenu createMenu;

    private final ObjectMapper objectMapper;

    @PostMapping("/secured/createMenu")
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request)
        throws DomainException, JsonProcessingException {

        LOG.info("Create Menu Request : [{}]", this.objectMapper.writeValueAsString(request));

        var output = this.createMenu.execute(new CreateMenu.Input(
            new MenuId(Long.parseLong(request.menuId())), request.menuName(), request.menuId(),
            request.isActive()));

        var response = new Response(output.menuId().getId().toString());

        LOG.info("Create Action Response : [{}]", this.objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(@NotNull @NotBlank @JsonProperty("menuId") String menuId,
                          @NotNull @NotBlank @JsonProperty("menuName") String menuName,
                          @NotNull @NotBlank @JsonProperty("parentId") String parentId,
                          @JsonProperty("isActive") boolean isActive) implements Serializable { }

    public record Response(@JsonProperty("menuId") String menuId) implements Serializable { }

}
