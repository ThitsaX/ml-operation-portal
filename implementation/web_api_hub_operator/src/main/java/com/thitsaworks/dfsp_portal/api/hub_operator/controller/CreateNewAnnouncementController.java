package com.thitsaworks.dfsp_portal.api.hub_operator.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.dfsp_portal.component.exception.DFSPPortalException;
import com.thitsaworks.dfsp_portal.usecase.hub_operator.CreateNewAnnouncement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.text.ParseException;
import java.time.Instant;

@RestController
public class CreateNewAnnouncementController {

    private static final Logger LOG = LoggerFactory.getLogger(CreateNewAnnouncementController.class);

    @Autowired
    private CreateNewAnnouncement createNewAnnouncement;

    @RequestMapping(value = "/secured/create_new_announcement", method = RequestMethod.POST)
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request)
            throws DFSPPortalException, ParseException {


        CreateNewAnnouncement.Output output = this.createNewAnnouncement.execute(
                new CreateNewAnnouncement.Input(request.announcementTitle, request.announcementDetail,Instant.parse(request.announcementDate)));

        return new ResponseEntity<>(new Response(output.isCreated()), HttpStatus.OK);
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Request implements Serializable {

        @NotNull
        @JsonProperty("announcement_title")
        private String announcementTitle;

        @NotNull
        @JsonProperty("announcement_detail")
        private String announcementDetail;

        @NotNull
        @JsonProperty("announcement_date")
        private String announcementDate;


    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Response implements Serializable {

        @JsonProperty("created")
        private boolean created;

    }

}
