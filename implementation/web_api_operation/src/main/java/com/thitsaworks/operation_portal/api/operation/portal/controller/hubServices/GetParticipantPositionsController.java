package com.thitsaworks.operation_portal.api.operation.portal.controller.hubServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.thitsaworks.operation_portal.api.operation.portal.security.UserContext;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantUserId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.hub_services.data.FinancialData;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetParticipantPositionsData;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetParticipantPositionsController {

    private static final Logger LOG = LoggerFactory.getLogger(GetParticipantPositionsController.class);

    private final GetParticipantPositionsData getParticipantPositionsData;

    @GetMapping("/secured/getParticipantPositionsData")
    public ResponseEntity<Response> execute() throws DomainException, JsonProcessingException {

        UserContext
            userContext =
            (UserContext) SecurityContextHolder.getContext()
                                               .getAuthentication()
                                               .getDetails();

        GetParticipantPositionsData.Output output =
                this.getParticipantPositionsData.execute(new GetParticipantPositionsData.Input(new ParticipantUserId(
                        userContext.userId()
                                   .getEntityId())));

        if (output != null && !output.financialData()
                                     .isEmpty()) {

            List<Response.ParticipantPositionsData> participantPositionsDataList = new ArrayList<>();

            for (FinancialData financialData : output.financialData()) {

                participantPositionsDataList.add(new Response.ParticipantPositionsData(financialData.dfspId(),
                                                                                       financialData.dfspName(),
                                                                                       financialData.currency(),
                                                                                       financialData.balance(),
                                                                                       financialData.currentPosition(),
                                                                                       ((financialData.ndcPercent() != null &&
                                                                         !financialData.ndcPercent()
                                                                                       .equals(BigDecimal.ZERO.setScale(
                                                                                               2,
                                                                                               RoundingMode.HALF_UP))) ?
                                                                         financialData.ndcPercent() + "%" : "-"),
                                                                                       financialData.ndc(),
                                                                                       (financialData.ndcUsed() != null) ?
                                                                         financialData.ndcUsed() : BigDecimal.ZERO,
                                                                                       financialData.participantSettlementCurrencyId(),
                                                                                       financialData.participantPositionCurrencyId()));
            }

            var response = new Response(participantPositionsDataList);

            return new ResponseEntity<>(response, HttpStatus.OK);

        } else {

            return new ResponseEntity<>(new Response(null), HttpStatus.OK);
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("participantPositionsData") List<ParticipantPositionsData> participantPositionsData) {

        public Response {

            participantPositionsData = participantPositionsData != null ? participantPositionsData : List.of();
        }

        public record ParticipantPositionsData(@JsonProperty("dfspId") String dfspId, @JsonProperty("dfspName") String dfspName,
                                               @JsonProperty("currency") String currency,
                                               @JsonProperty("balance") BigDecimal balance,
                                               @JsonProperty("currentPosition") BigDecimal currentPosition,
                                               @JsonProperty("ndcPercent") String ndcPercent, @JsonProperty("ndc") BigDecimal ndc,
                                               @JsonProperty("ndcUsed") BigDecimal ndcUsed,
                                               @JsonProperty("participantSettlementCurrencyId") Integer participantSettlementCurrencyId,
                                               @JsonProperty("participantPositionCurrencyId") Integer participantPositionCurrencyId) {
        }

    }

}
