package com.thitsaworks.operation_portal.api.participant.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.operation_portal.ledger.data.FinancialData;
import com.thitsaworks.operation_portal.usecase.central_ledger.GetDashboardData;
import com.thitsaworks.operation_portal.api.participant.security.UserContext;
import com.thitsaworks.operation_portal.component.exception.DFSPPortalException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
public class GetDashboardDataController {

    private static final Logger LOG = LoggerFactory.getLogger(GetDashboardDataController.class);

    @Autowired
    private GetDashboardData getDashboardData;

    @RequestMapping(value = "/secured/get_dashboard_data", method = RequestMethod.GET)
    public ResponseEntity<Response> execute() throws DFSPPortalException {

        UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication().getDetails();

        GetDashboardData.Output output =
                this.getDashboardData.execute(new GetDashboardData.Input(userContext.getParticipantUserId()));

        if (output != null && !output.getFinancialData().isEmpty()) {

            List<Response.FinancialData> financialDataList = new ArrayList<>();

            for (FinancialData financialData : output.getFinancialData()) {

                financialDataList.add(
                        new Response.FinancialData(financialData.getDfspName(), financialData.getCurrency(),
                                financialData.getBalance(), financialData.getCurrentPosition(), financialData.getNdc(),
                                (financialData.getNdcUsed() != null) ? financialData.getNdcUsed() : BigDecimal.ZERO));
            }

            return new ResponseEntity<>(new GetDashboardDataController.Response(financialDataList), HttpStatus.OK);

        } else {

            return new ResponseEntity<>(new Response(), HttpStatus.OK);
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Response implements Serializable {

        @JsonProperty("financial_data")
        protected List<FinancialData> financialData;

        @Getter
        @AllArgsConstructor
        public static class FinancialData implements Serializable {

            @JsonProperty("dfsp_name")
            protected String dfspName;

            @JsonProperty("currency")
            protected String currency;

            @JsonProperty("balance")
            protected BigDecimal balance;

            @JsonProperty("current_position")
            protected BigDecimal currentPosition;

            @JsonProperty("ndc")
            protected BigDecimal ndc;

            @JsonProperty("ndc_used")
            protected BigDecimal ndcUsed;

        }
    }
}
