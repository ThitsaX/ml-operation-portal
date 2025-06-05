package com.thitsaworks.dfsp_portal.usecase.hub_operator.impl;

import com.thitsaworks.dfsp_portal.audit.exception.UserNotFoundException;
import com.thitsaworks.dfsp_portal.datasource.persistence.WriteTransactional;
import com.thitsaworks.dfsp_portal.hubuser.query.GetAnnouncements;
import com.thitsaworks.dfsp_portal.usecase.hub_operator.GetAllAnnouncement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetAllAnnouncementBean extends GetAllAnnouncement {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllAnnouncementBean.class);

    @Autowired
    private GetAnnouncements getAnnouncements;

    @Override
    @WriteTransactional
    public GetAllAnnouncement.Output onExecute(GetAllAnnouncement.Input input) throws Exception {

        GetAnnouncements.Output output = this.getAnnouncements.execute(new GetAnnouncements.Input());

        List<GetAllAnnouncement.Output.AnnouncementInfo> announcementInfoList = new ArrayList<>();

        for (GetAnnouncements.Output.AnnouncementInfo data : output.getAnnouncementInfoList()) {

            announcementInfoList.add(
                    new GetAllAnnouncement.Output.AnnouncementInfo(data.getAnnouncementId(),
                            data.getAnnouncementTitle(), data.getAnnouncementDetail(),
                            data.getAnnouncementDate()));
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
