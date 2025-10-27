package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetAnnouncementInfoList;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetAnnouncementInfoListController {

    private static final Logger LOG = LoggerFactory.getLogger(GetAnnouncementInfoListController.class);

    private final GetAnnouncementInfoList getAnnouncementInfoList;

    @GetMapping(value = "/public/getAnnouncements")
    public ResponseEntity<Response> execute() throws DomainException, JsonProcessingException {

        GetAnnouncementInfoList.Output
            output =
            this.getAnnouncementInfoList.execute(new GetAnnouncementInfoList.Input());

        List<Response.AnnouncementInfo> announcementInfoList = new ArrayList<>();

        for (var announcement : output.announcementInfoList()) {

            announcementInfoList.add(new Response.AnnouncementInfo(announcement.announcementId()
                                                                               .getId()
                                                                               .toString(),
                                                                   announcement.announcementTitle(),
                                                                   announcement.announcementDetail(),
                                                                   announcement.announcementDate()
                                                                               .getEpochSecond()));
        }

        if (!announcementInfoList.isEmpty()) {

            announcementInfoList.sort(Comparator.comparing(Response.AnnouncementInfo::announcementDate)
                                                .reversed());

        }
        Response response = new Response(announcementInfoList);

        LOG.info("Get Announcement Info List Response: [{}]", response);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("announcementInfoList") List<AnnouncementInfo> announcementInfoList)
        implements Serializable {

        @JsonIgnoreProperties(ignoreUnknown = true)
        public record AnnouncementInfo(@JsonProperty("id") String announcementId,
                                       @JsonProperty("title") String announcementTitle,
                                       @JsonProperty("detail") String announcementDetail,
                                       @JsonProperty("date") long announcementDate) implements Serializable {
        }

    }

}
