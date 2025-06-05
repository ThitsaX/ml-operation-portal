package com.thitsaworks.dfsp_portal.api.participant.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsa.dfsp_portal.usecase.central_ledger.GetAllTransfer;
import com.thitsaworks.dfsp_portal.api.participant.security.UserContext;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class GetAllTransferController {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllTransferController.class);

    @Autowired
    private GetAllTransfer getAllTransfer;
    private TimeZoneConverter timeZoneConverter;

    @RequestMapping(value = "/secured/get_all_transfer", method = RequestMethod.GET)
    public ResponseEntity<Response> execute(
            @Valid @RequestParam String fromDate, String toDate, String transferId, String payerFspId,
            String payeeFspId,
            String payerIdentifierTypeId, String payeeIdentifierTypeId, String payerIdentifierValue,
            String payeeIdentifierValue, String currencyId, String transferStateId, String timezone) throws DFSPPortalException, ParseException {

        UserContext userContext =
                (UserContext) SecurityContextHolder.getContext().getAuthentication().getDetails();

        String localFromDate = timeZoneConverter.convertTimeZone(fromDate, timezone);
        String localToDate = timeZoneConverter.convertTimeZone(toDate, timezone);

        String showTimezone;
        if (timezone.startsWith("-"))
        {
            showTimezone= timezone.replaceFirst(".","+");
        }
        else
        {
            showTimezone= timezone.replaceFirst(".","-");
        }


        GetAllTransfer.Output output = this.getAllTransfer.execute(
                new GetAllTransfer.Input(
                        localFromDate,
                        localToDate,
                        transferId,
                        payerFspId,
                        payeeFspId,
                        payerIdentifierTypeId,
                        payeeIdentifierTypeId,
                        payerIdentifierValue,
                        payeeIdentifierValue,
                        currencyId,
                        transferStateId,
                        userContext.getParticipantUserId(),
                        timezone));

        List<Response.TransferInfo> transferInfoList = new ArrayList<>();

        for (var transferInfo : output.getTransferInfoList()) {

            transferInfoList.add(new Response.TransferInfo(
                            transferInfo.getTransferId(),
                            transferInfo.getState(),
                            transferInfo.getType(),
                            transferInfo.getCurrency(),
                            transferInfo.getAmount(),
                            transferInfo.getPayerDfsp(),
                            transferInfo.getPayeeDfsp(),
                            transferInfo.getSettlementBatch(),
                            (timeZoneConverter.convertTimeZone(transferInfo.getSubmittedOnDate().replace(" ", "T") + "Z", showTimezone)+timezone).replace("T"," ").replace("Z"," "))) ;


        }

        return new ResponseEntity<>(new Response(transferInfoList), HttpStatus.OK);
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Response implements Serializable {

        @JsonProperty("transfer_info_list")
        protected List<TransferInfo> transferInfoList;

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

            @JsonProperty("payer_dfsp")
            protected String payerDfsp;

            @JsonProperty("payee_dfsp")
            protected String payeeDfsp;

            @JsonProperty("settlement_batch")
            protected String settlementBatch;

            @JsonProperty("submitted_on_date")
            protected String submittedOnDate;

        }
    }
}
