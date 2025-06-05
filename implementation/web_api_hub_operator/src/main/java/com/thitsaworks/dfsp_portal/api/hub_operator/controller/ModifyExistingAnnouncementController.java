package com.thitsaworks.dfsp_portal.api.hub_operator.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.dfsp_portal.component.exception.DFSPPortalException;
import com.thitsaworks.dfsp_portal.hubuser.identity.AnnouncementId;
import com.thitsaworks.dfsp_portal.usecase.hub_operator.ModifyExistingAnnouncement;
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
import java.time.Instant;

@RestController
public class ModifyExistingAnnouncementController {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyExistingAnnouncementController.class);

    @Autowired
    private ModifyExistingAnnouncement modifyExistingAnnouncement;

    @RequestMapping(value = "/secured/modify_announcement", method = RequestMethod.POST)
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request) throws DFSPPortalException {

        ModifyExistingAnnouncement.Output output = this.modifyExistingAnnouncement.execute(
                new ModifyExistingAnnouncement.Input(new AnnouncementId(Long.parseLong(request.announcementId)),
                        request.announcementTitle, request.announcementDetail, Instant.parse(request.announcement_date),
                        request.isDeleted));

        return new ResponseEntity<>(new Response(output.isModified()), HttpStatus.OK);
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Request implements Serializable {

        @NotNull
        @JsonProperty("announcement_id")
        private String announcementId;

        @NotNull
        @JsonProperty("announcement_title")
        private String announcementTitle;

        @NotNull
        @JsonProperty("announcement_detail")
        private String announcementDetail;

        @NotNull
        @JsonProperty("announcement_date")
        private String announcement_date;

        @NotNull
        @JsonProperty("is_deleted")
        private boolean isDeleted;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Response implements Serializable {

        @JsonProperty("modified")
        private boolean modified;

    }
}
