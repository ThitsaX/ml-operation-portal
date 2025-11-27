package com.thitsaworks.operation_portal.api.operation.portal.controller.hubServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.thitsaworks.operation_portal.api.operation.portal.security.UserContext;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.FinalizeSettlement;
import jakarta.validation.Valid;
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
import java.util.List;

@RestController
@RequiredArgsConstructor
public class FinalizeSettlementController {

    private static final Logger LOG = LoggerFactory.getLogger(FinalizeSettlementController.class);

    private final FinalizeSettlement finalizeSettlement;

    @PostMapping(value = "/secured/finalizeSettlement")
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request)
        throws DomainException, JsonProcessingException {

        LOG.info("Finalize Settlement Request : [{}]", request);

        UserContext userContext =
            (UserContext) SecurityContextHolder.getContext()
                                               .getAuthentication()
                                               .getDetails();

        var output = this.finalizeSettlement.execute(new FinalizeSettlement.Input(request.settlementId()));

        var response = new FinalizeSettlementController.Response(output.settlementId(),
                                                                 output.settlementWindowIds(),
                                                                 output.windowOpenedDate(),
                                                                 output.windowClosedDate(),
                                                                 output.details()
                                                                       .stream()
                                                                       .map(detail -> new Detail(
                                                                           detail.participantName(),
                                                                           detail.participantLimit(),
                                                                           detail.participantBalance(),
                                                                           detail.debitAmount()
                                                                                 .abs(),
                                                                           detail.creditAmount()
                                                                                 .abs(),
                                                                           detail.currency()
                                                                       ))
                                                                       .toList());

        LOG.info("Finalize Settlement Response : [{}]", response);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(@JsonProperty("settlementId") Integer settlementId
    ) implements Serializable { }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("settlementId") int settlementId,
                           @JsonProperty("settlementWindowIds") String settlementWindowIds,
                           @JsonProperty("windowOpenedDate") String windowOpenedDate,
                           @JsonProperty("windowClosedDate") String windowClosedDate,
                           @JsonProperty("details") List<Detail> details
    ) implements Serializable { }

    public record Detail(@JsonProperty("participantName") String participantName,
                         @JsonProperty("participantLimit") BigDecimal participantLimit,
                         @JsonProperty("participantBalance") BigDecimal participantBalance,
                         @JsonProperty("debitAmount") BigDecimal debitAmount,
                         @JsonProperty("creditAmount") BigDecimal creditAmount,
                         @JsonProperty("currency") String currency) implements Serializable { }

}