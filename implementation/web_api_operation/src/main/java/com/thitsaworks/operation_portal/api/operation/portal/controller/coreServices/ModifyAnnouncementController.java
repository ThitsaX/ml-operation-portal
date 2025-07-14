package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.AnnouncementId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.core_services.ModifyAnnouncement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.time.Instant;

@RestController
@RequiredArgsConstructor
public class ModifyAnnouncementController {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyAnnouncementController.class);

    private final ModifyAnnouncement modifyExistingAnnouncement;

    private final ObjectMapper objectMapper;

    @PostMapping(value = "/secured/modifyAnnouncement")
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request)
            throws DomainException, JsonProcessingException {

        LOG.info("Modify announcement request: {}", objectMapper.writeValueAsString(request));

        ModifyAnnouncement.Output output = this.modifyExistingAnnouncement.execute(
                new ModifyAnnouncement.Input(new AnnouncementId(Long.parseLong(request.announcementId)),
                        request.announcementTitle, request.announcementDetail, Instant.parse(request.announcementDate),
                        request.isDeleted));

        Response response = new Response(output.modified());

        LOG.info("Modify announcement response: {}", objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(
            @NotNull
            @JsonProperty("announcementId")
            String announcementId,

            @NotNull
            @JsonProperty("announcementTitle")
            String announcementTitle,

            @NotNull
            @JsonProperty("announcementDetail")
            String announcementDetail,

            @NotNull
            @JsonProperty("announcementDate")
            String announcementDate,

            @NotNull
            @JsonProperty("isDeleted")
            boolean isDeleted) implements Serializable {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
            @JsonProperty("isModified")
            boolean isModified) implements Serializable {
    }

}
