package com.thitsaworks.operation_portal.api.operation.portal.controller.hubServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.api.operation.portal.security.UserContext;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantUserId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.hub_services.data.FinancialData;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetDashboardData;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetDashboardDataController {

    private static final Logger LOG = LoggerFactory.getLogger(GetDashboardDataController.class);

    private final GetDashboardData getDashboardData;

    private final ObjectMapper objectMapper;

    @GetMapping("/secured/getDashboardData")
    public ResponseEntity<Response> execute() throws DomainException, JsonProcessingException {

        LOG.info("Get dashboard data request : {}", "");

        UserContext
            userContext =
            (UserContext) SecurityContextHolder.getContext()
                                               .getAuthentication()
                                               .getDetails();

        GetDashboardData.Output output =
            this.getDashboardData.execute(new GetDashboardData.Input(new ParticipantUserId(userContext.userId()
                                                                                                      .getEntityId())));

        if (output != null && !output.financialData()
                                     .isEmpty()) {

            List<Response.FinancialData> financialDataList = new ArrayList<>();

            for (FinancialData financialData : output.financialData()) {

                financialDataList.add(
                    new Response.FinancialData(financialData.getDfspName(),
                                               financialData.getCurrency(),
                                               financialData.getBalance(),
                                               financialData.getCurrentPosition(),
                                               financialData.getNdc(),
                                               (financialData.getNdcUsed() != null) ? financialData.getNdcUsed() :
                                                   BigDecimal.ZERO));
            }

            var response = new Response(financialDataList);

            return new ResponseEntity<>(response, HttpStatus.OK);

        } else {

            return new ResponseEntity<>(new Response(null), HttpStatus.OK);
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("financialData") List<FinancialData> financialData) {

        public Response {

            financialData = financialData != null ? financialData : List.of();
        }

        public record FinancialData(
            @JsonProperty("dfspName") String dfspName,
            @JsonProperty("currency") String currency,
            @JsonProperty("balance") BigDecimal balance,
            @JsonProperty("currentPosition") BigDecimal currentPosition,
            @JsonProperty("ndc") BigDecimal ndc,
            @JsonProperty("ndcUsed") BigDecimal ndcUsed) {
        }

    }

}
