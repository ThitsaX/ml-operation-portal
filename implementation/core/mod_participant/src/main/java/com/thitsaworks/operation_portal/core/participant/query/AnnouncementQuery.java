package com.thitsaworks.operation_portal.core.participant.query;

import com.thitsaworks.operation_portal.component.common.identifier.AnnouncementId;
import com.thitsaworks.operation_portal.core.participant.data.AnnouncementData;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;

import java.util.List;

public interface AnnouncementQuery {

    List<AnnouncementData> getAnnouncements();

    AnnouncementData get(AnnouncementId announcementId) throws ParticipantException;

}

