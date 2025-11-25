package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.thitsaworks.operation_portal.api.operation.portal.security.UserContext;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.CreateApprovalRequest;
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
import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
public class CreateApprovalRequestController {

    private static final Logger LOG = LoggerFactory.getLogger(CreateApprovalRequestController.class);

    private final CreateApprovalRequest createApprovalRequest;

    @PostMapping(value = "/secured/createApprovalRequest")
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request)
        throws JsonProcessingException, DomainException {

        LOG.info("Create Approval Request : [{}]", request);

        UserContext userContext =
            (UserContext) SecurityContextHolder.getContext()
                                               .getAuthentication()
                                               .getDetails();

        var output = this.createApprovalRequest.execute(new CreateApprovalRequest.Input(request.requestedAction(),
                                                                                        request.participantName(),
                                                                                        request.currency,
                                                                                        request.settlementCurrencyId(),
                                                                                        request.positionCurrencyId(),
                                                                                        request.amount(),
                                                                                        userContext.userId()));

        var response = new Response(output.approvalRequestId()
                                          .getEntityId()
                                          .toString());

        LOG.info("Create Approval Request Response : [{}]", response);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(@NotNull @NotBlank @JsonProperty("requestedAction") String requestedAction,
                          @NotNull @NotBlank @JsonProperty("participantName") String participantName,
                          @NotNull @NotBlank @JsonProperty("currency") String currency,
                          @NotNull @NotBlank @JsonProperty("settlementCurrencyId") String settlementCurrencyId,
                          @NotNull @NotBlank @JsonProperty("positionCurrencyId") String positionCurrencyId,
                          @JsonProperty("amount") BigDecimal amount) implements Serializable { }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("approvalRequestId") String approvalRequestId) implements Serializable { }

}
