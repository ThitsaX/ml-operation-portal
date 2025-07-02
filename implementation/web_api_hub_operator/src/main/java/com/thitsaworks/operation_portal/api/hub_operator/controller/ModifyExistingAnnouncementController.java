package com.thitsaworks.operation_portal.api.hub_operator.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.AnnouncementId;
import com.thitsaworks.operation_portal.component.misc.exception.OperationPortalException;
import com.thitsaworks.operation_portal.usecase.hub_operator.ModifyExistingAnnouncement;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;

@RestController
@RequiredArgsConstructor
public class ModifyExistingAnnouncementController {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyExistingAnnouncementController.class);

    private final ModifyExistingAnnouncement modifyExistingAnnouncement;

    private final ObjectMapper objectMapper;

    @PostMapping(value = "/secured/modify_announcement")
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request)
            throws OperationPortalException, JsonProcessingException {

        LOG.info("Modify announcement request: {}", objectMapper.writeValueAsString(request));

        ModifyExistingAnnouncement.Output output = this.modifyExistingAnnouncement.execute(
                new ModifyExistingAnnouncement.Input(new AnnouncementId(Long.parseLong(request.announcementId)),
                        request.announcementTitle, request.announcementDetail, Instant.parse(request.announcement_date),
                        request.isDeleted));

        Response response = new Response(output.modified());

        LOG.info("Modify announcement response: {}", objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(
            @NotNull
            @JsonProperty("announcement_id")
            String announcementId,

            @NotNull
            @JsonProperty("announcement_title")
            String announcementTitle,

            @NotNull
            @JsonProperty("announcement_detail")
            String announcementDetail,

            @NotNull
            @JsonProperty("announcement_date")
            String announcement_date,

            @NotNull
            @JsonProperty("is_deleted")
            boolean isDeleted) implements Serializable {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
            @JsonProperty("modified")
            boolean modified) implements Serializable {
    }

}
