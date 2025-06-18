package com.thitsaworks.operation_portal.core.hubuser.query;

import com.thitsaworks.operation_portal.component.common.identifier.AnnouncementId;
import com.thitsaworks.operation_portal.core.hubuser.data.AnnouncementData;
import com.thitsaworks.operation_portal.core.hubuser.exception.AnnouncementNotFoundException;

import java.util.List;

public interface AnnouncementQuery {

    List<AnnouncementData> getAnnouncements();

    AnnouncementData get(AnnouncementId announcementId) throws AnnouncementNotFoundException;

}

