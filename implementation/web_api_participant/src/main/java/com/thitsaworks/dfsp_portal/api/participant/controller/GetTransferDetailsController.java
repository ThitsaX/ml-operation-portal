package com.thitsaworks.dfsp_portal.api.participant.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsa.dfsp_portal.usecase.central_ledger.GetTransferDetails;
import com.thitsaworks.dfsp_portal.component.exception.DFSPPortalException;
import com.thitsaworks.dfsp_portal.component.util.TimeZoneConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;

@RestController
public class GetTransferDetailsController {

    private static final Logger LOG = LoggerFactory.getLogger(GetTransferDetailsController.class);

    @Autowired
    private GetTransferDetails getTransferDetails;

    private TimeZoneConverter timeZoneConverter;

    @RequestMapping(value = "/secured/get_transfer_detail", method = RequestMethod.GET)
    public ResponseEntity<Response> execute(
            @Valid @RequestParam String transferId, String timezone) throws DFSPPortalException, ParseException {

        GetTransferDetails.Output output = this.getTransferDetails.execute(new GetTransferDetails.Input(transferId));
        String convertedDate;
        var info = output.getBusinessData();

        String showTimezone;
        if (timezone.startsWith("-"))
        {
            showTimezone= timezone.replaceFirst(".","+");
        }
        else
        {
            showTimezone= timezone.replaceFirst(".","-");
        }

             convertedDate= (timeZoneConverter.convertTimeZone(info.getSubmittedOnDate().replace(" ", "T") + "Z",showTimezone)+timezone).replace("T"," ").replace("Z"," ") ;


        Response.TransferInfo transferInfo =
                new Response.TransferInfo(info.getTransferId(), info.getState(), info.getType(), info.getCurrency(),
                        info.getAmount(), info.getPayer(), info.getPayerDetails(), info.getPayerDfsp(), info.getPayee(),
                        info.getPayeeDetails(), info.getPayeeDfsp(), info.getSettlementBatch(),convertedDate);

        return new ResponseEntity<>(new Response(transferInfo), HttpStatus.OK);
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Response implements Serializable {

        @JsonProperty("transfer_details")
        protected TransferInfo transferInfo;

        @Getter
        @AllArgsConstructor
        public static class TransferInfo implements Serializable {

            @JsonProperty("transfer_id")
            protected String transferId;

            @JsonProperty("state")
            protected String state;

            @JsonProperty("type")
            protected String type;

            @JsonProperty("currency")
            protected String currency;

            @JsonProperty("amount")
            protected BigDecimal amount;

            @JsonProperty("payer")
            protected String payer;

            @JsonProperty("payer_details")
            protected String payerDetails;

            @JsonProperty("payer_dfsp")
            protected String payerDfsp;

            @JsonProperty("payee")
            protected String payee;

            @JsonProperty("payee_details")
            protected String payeeDetails;

            @JsonProperty("payee_dfsp")
            protected String payeeDfsp;

            @JsonProperty("settlement_batch")
            protected String settlementBatch;

            @JsonProperty("submitted_on_date")
            protected String submittedOnDate;

        }

    }

}
