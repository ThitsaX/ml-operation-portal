package com.thitsaworks.dfsp_portal.api.participant.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsa.dfsp_portal.usecase.central_ledger.GetAllIDType;
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
public class GetAllIDTypeController {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllIDTypeController.class);

    @Autowired
    private GetAllIDType getAllIDType;

    @RequestMapping(value = "/secured/get_all_id_type", method = RequestMethod.GET)
    public ResponseEntity<Response> execute() throws DFSPPortalException {

        GetAllIDType.Output output = this.getAllIDType.execute(new GetAllIDType.Input());

        List<Response.IDTypeInfo> idTypeInfoList = new ArrayList<>();

        for (var idType : output.getIdTypeDataList()) {

            idTypeInfoList.add(new Response.IDTypeInfo(
                            idType.getPartyIdentifierTypeId(),
                            idType.getName()));
        }

        return new ResponseEntity<>(new Response(idTypeInfoList), HttpStatus.OK);
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Response implements Serializable {

        @JsonProperty("id_type_list")
        protected List<IDTypeInfo> idTypeInfoList;

        @Getter
        @AllArgsConstructor
        public static class IDTypeInfo implements Serializable {

            @JsonProperty("party_identifier_type_id")
            protected String partyIdentifierTypeId;

            @JsonProperty("name")
            protected String name;

        }

    }

}
