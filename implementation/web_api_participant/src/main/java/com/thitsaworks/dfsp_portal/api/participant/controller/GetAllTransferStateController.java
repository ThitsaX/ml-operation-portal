package com.thitsaworks.dfsp_portal.api.participant.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsa.dfsp_portal.usecase.central_ledger.GetAllTransferState;
import com.thitsaworks.dfsp_portal.component.exception.DFSPPortalException;
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
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@RestController
public class GetAllTransferStateController {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllTransferStateController.class);

    @Autowired
    private GetAllTransferState getAllTransferState;

    @RequestMapping(value = "/secured/get_all_transfer_state", method = RequestMethod.GET)
    public ResponseEntity<Response> execute() throws DFSPPortalException {

        GetAllTransferState.Output output = this.getAllTransferState.execute(new GetAllTransferState.Input());

        List<Response.TransferStateInfo> transferStateInfoList = new ArrayList<>();

        for (var idType : output.getTransferStateDataList()) {

            transferStateInfoList.add(new Response.TransferStateInfo(idType.getTransferStateId(),
                            idType.getTransferState()));
        }

        return new ResponseEntity<>(new Response(transferStateInfoList), HttpStatus.OK);
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Response implements Serializable {

        @JsonProperty("transfer_state_list")
        protected List<TransferStateInfo> transferStateInfoList;

        @Getter
        @AllArgsConstructor
        public static class TransferStateInfo implements Serializable {

            @JsonProperty("transfer_state_id")
            protected String transferStateId;

            @JsonProperty("transfer_state")
            protected String transferState;

        }

    }

}
