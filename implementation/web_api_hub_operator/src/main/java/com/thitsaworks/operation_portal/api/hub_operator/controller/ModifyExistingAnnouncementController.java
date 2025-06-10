package com.thitsaworks.operation_portal.api.hub_operator.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.operation_portal.component.exception.DFSPPortalException;
import com.thitsaworks.operation_portal.hubuser.identity.AnnouncementId;
import com.thitsaworks.operation_portal.usecase.hub_operator.ModifyExistingAnnouncement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

        @NonNull
        @JsonProperty("announcement_id")
        private String announcementId;

        @NonNull
        @JsonProperty("announcement_title")
        private String announcementTitle;

        @NonNull
        @JsonProperty("announcement_detail")
        private String announcementDetail;

        @NonNull
        @JsonProperty("announcement_date")
        private String announcement_date;

        @NonNull
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
