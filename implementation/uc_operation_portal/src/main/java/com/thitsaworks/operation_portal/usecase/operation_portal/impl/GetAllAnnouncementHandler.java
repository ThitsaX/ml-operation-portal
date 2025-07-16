package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.core.hubuser.data.AnnouncementData;
import com.thitsaworks.operation_portal.core.hubuser.query.AnnouncementQuery;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetAllAnnouncement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetAllAnnouncementHandler implements GetAllAnnouncement {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllAnnouncementHandler.class);

    private final AnnouncementQuery announcementQuery;

    @Autowired
    public GetAllAnnouncementHandler(AnnouncementQuery announcementQuery) {

        this.announcementQuery = announcementQuery;
    }

    @Override
    public Output execute(Input input) {

        List<AnnouncementData> announcementDataList = this.announcementQuery.getAnnouncements();

        List<GetAllAnnouncement.Output.AnnouncementInfo> announcementInfoList = new ArrayList<>();

        for (AnnouncementData announcementData : announcementDataList) {

            announcementInfoList.add(
                    new Output.AnnouncementInfo(announcementData.announcementId(),
                                                               announcementData.announcementTitle(),
                                                               announcementData.announcementDetail(),
                                                               announcementData.announcementDate()));
        }

        return new Output(announcementInfoList);
    }

}
