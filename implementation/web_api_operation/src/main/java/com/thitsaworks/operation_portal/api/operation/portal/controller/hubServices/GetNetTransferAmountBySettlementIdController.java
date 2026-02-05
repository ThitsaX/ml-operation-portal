package com.thitsaworks.operation_portal.api.operation.portal.controller.hubServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.api.operation.portal.security.UserContext;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetNetTransferAmountBySettlementId;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetNetTransferAmountBySettlementIdController {

    private static final Logger LOG = LoggerFactory.getLogger(GetNetTransferAmountBySettlementIdController.class);

    private final GetNetTransferAmountBySettlementId getNetTransferAmountBySettlementId;

    private final ObjectMapper objectMapper;

    @GetMapping(value = "/secured/getNetTransferAmountBySettlementId")
    public ResponseEntity<GetNetTransferAmountBySettlementIdController.Response> execute(@RequestParam
                                                                                         @NotNull(message = "settlementId is required")
                                                                                         int settlementId)

        throws DomainException, JsonProcessingException {

        LOG.info("Get Net Transfer Amount By Settlement Id Request : [{}]", settlementId);

        UserContext userContext =
            (UserContext) SecurityContextHolder.getContext()
                                               .getAuthentication()
                                               .getDetails();
        var
            output =
            this.getNetTransferAmountBySettlementId.execute(new GetNetTransferAmountBySettlementId.Input(
                settlementId));

        var response = new Response(output.settlementId(),
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
                                              detail.ndcPercent(),
                                              detail.currency()
                                          ))
                                          .toList()
        );

        LOG.info("Get Net Transfer Amount By Window Id Response : [{}]",
                 this.objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

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
                         @JsonProperty("ndcPercent") BigDecimal ndcPercent,
                         @JsonProperty("currency") String currency) implements Serializable { }

}