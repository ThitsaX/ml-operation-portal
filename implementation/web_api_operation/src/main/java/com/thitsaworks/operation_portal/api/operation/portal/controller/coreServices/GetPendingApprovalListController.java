package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetPendingApprovalList;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetPendingApprovalListController {

    private static final Logger LOG = LoggerFactory.getLogger(GetPendingApprovalListController.class);

    private final GetPendingApprovalList getPendingApprovalList;

    private final ObjectMapper objectMapper;

    @GetMapping(value = "/secured/getPendingApprovalList")
    public ResponseEntity<Response> execute() throws DomainException, JsonProcessingException {

        var output = this.getPendingApprovalList.execute(new GetPendingApprovalList.Input());

        var response = new Response(output.pendingApprovalList()
                                          .stream()
                                          .sorted(Comparator.comparing(
                                              request -> request.requestedDateTime()
                                                                .getEpochSecond(),
                                              Comparator.reverseOrder()))
                                          .map(request -> new Response.PendingApproval(request.approvalRequestId()
                                                                                              .getEntityId()
                                                                                              .toString(),
                                                                                       request.requestedAction(),
                                                                                       request.participantName(),
                                                                                       request.currency(),
                                                                                       request.amount(),
                                                                                       request.requestedBy(),
                                                                                       request.requestedDateTime()
                                                                                              .getEpochSecond(),
                                                                                       request.action()
                                                                                              .name()))
                                          .toList());

        LOG.info("Get Pending Approval List Response : [{}]", this.objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    public record Request() implements Serializable { }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("pendingApprovalList") List<PendingApproval> pendingApprovalList)
        implements Serializable {

        public record PendingApproval(@JsonProperty("approvalRequestId") String approvalRequestId,
                                      @JsonProperty("requestedAction") String requestedAction,
                                      @JsonProperty("participantName") String participantName,
                                      @JsonProperty("currency") String currency,
                                      @JsonProperty("amount") BigDecimal amount,
                                      @JsonProperty("requestedBy") String requestedBy,
                                      @JsonProperty("requestedDateTime") long requestedDateTime,
                                      @JsonProperty("action") String action) implements Serializable { }

    }

}
