package com.thitsaworks.operation_portal.usecase.hub_operator.impl;

import com.thitsaworks.operation_portal.core.audit.exception.UserNotFoundException;
import com.thitsaworks.operation_portal.core.hubuser.data.AnnouncementData;
import com.thitsaworks.operation_portal.core.hubuser.query.AnnouncementQuery;
import com.thitsaworks.operation_portal.usecase.hub_operator.GetAllAnnouncement;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class GetAllAnnouncementHandler extends GetAllAnnouncement {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllAnnouncementHandler.class);

    private final AnnouncementQuery announcementQuery;

    @Override
    public GetAllAnnouncement.Output onExecute(GetAllAnnouncement.Input input) throws Exception {

        List<AnnouncementData> announcementDataList = this.announcementQuery.getAnnouncements();

        List<GetAllAnnouncement.Output.AnnouncementInfo> announcementInfoList = new ArrayList<>();

        for (AnnouncementData announcementData : announcementDataList) {

            announcementInfoList.add(
                    new GetAllAnnouncement.Output.AnnouncementInfo(announcementData.announcementId(),
                                                                   announcementData.announcementTitle(),
                                                                   announcementData.announcementDetail(),
                                                                   announcementData.announcementDate()));
        }

        return new GetAllAnnouncement.Output(announcementInfoList);
    }

    @Override
    protected String getName() {

        return GetAllAnnouncement.class.getCanonicalName();
    }

    @Override
    protected String getDescription() {

        return null;
    }

    @Override
    protected String getScope() {

        return "uc_hub_operator";
    }

    @Override
    protected String getId() {

        return GetAllAnnouncement.class.getName();
    }

    @Override
    public boolean isOwned(Object userDetails) {

        return true;
    }

    @Override
    public boolean isAuthorized(Object userDetails) {

        return true;
    }

    @Override
    public void onAudit(GetAllAnnouncement.Input input, GetAllAnnouncement.Output output) throws UserNotFoundException {
        // do not need to audit because it is no need to login
    }

}
