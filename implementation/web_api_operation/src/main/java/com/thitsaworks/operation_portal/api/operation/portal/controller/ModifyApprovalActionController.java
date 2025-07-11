package com.thitsaworks.operation_portal.api.operation.portal.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.api.operation.portal.security.UserContext;
import com.thitsaworks.operation_portal.component.common.identifier.ApprovalRequestId;
import com.thitsaworks.operation_portal.component.common.type.ApprovalActionType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.core_services.ModifyApprovalAction;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

@RestController
@RequiredArgsConstructor
public class ModifyApprovalActionController {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyApprovalActionController.class);

    private final ModifyApprovalAction modifyApprovalAction;

    private final ObjectMapper objectMapper;

    @PostMapping(value = "/secured/modifyApprovalAction")
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request)
        throws JsonProcessingException, DomainException {

        LOG.info("Update Approval Request : {}", this.objectMapper.writeValueAsString(request));

        UserContext userContext =
            (UserContext) SecurityContextHolder.getContext()
                                               .getAuthentication()
                                               .getDetails();

        var
            output =
            this.modifyApprovalAction.execute(new ModifyApprovalAction.Input(new ApprovalRequestId(Long.parseLong(
                request.approvalRequestId())),
                                                                             ApprovalActionType.valueOf(request.action()),
                                                                             userContext.userId()));

        var response = new Response(output.approvalRequestId()
                                          .getEntityId()
                                          .toString());

        LOG.info("Update Approval Response : {}", this.objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(@NotNull @NotBlank String approvalRequestId,
                          @NotNull @NotBlank String action) implements Serializable { }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(String approvalRequestId) implements Serializable { }

}
