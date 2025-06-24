package com.thitsaworks.operation_portal.api.participant.controller.reporting;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.api.participant.security.UserContext;
import com.thitsaworks.operation_portal.component.misc.exception.OperationPortalException;
import com.thitsaworks.operation_portal.reporting.central_ledger.data.FinancialData;
import com.thitsaworks.operation_portal.usecase.central_ledger.GetDashboardData;
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

    @GetMapping("/secured/get_dashboard_data")
    public ResponseEntity<Response> execute() throws OperationPortalException, JsonProcessingException {

        LOG.info("Get dashboard data request : {}", "");

        UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication().getDetails();

        GetDashboardData.Output output =
                this.getDashboardData.execute(new GetDashboardData.Input(userContext.participantUserId()));

        if (output != null && !output.financialData().isEmpty()) {

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

            LOG.info("Get dashboard data response : {}", this.objectMapper.writeValueAsString(response));

            return new ResponseEntity<>(response, HttpStatus.OK);

        } else {

            return new ResponseEntity<>(new Response(null), HttpStatus.OK);
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("financial_data") List<FinancialData> financialData) {

        public Response {

            financialData = financialData != null ? financialData : List.of();
        }

        public record FinancialData(
                @JsonProperty("dfsp_name") String dfspName,
                @JsonProperty("currency") String currency,
                @JsonProperty("balance") BigDecimal balance,
                @JsonProperty("current_position") BigDecimal currentPosition,
                @JsonProperty("ndc") BigDecimal ndc,
                @JsonProperty("ndc_used") BigDecimal ndcUsed) {
        }

    }

}
