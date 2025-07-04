package com.thitsaworks.operation_portal.usecase.hub_operator.impl;

import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.core.hubuser.data.AnnouncementData;
import com.thitsaworks.operation_portal.core.hubuser.query.AnnouncementQuery;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.usecase.HubOperatorUseCase;
import com.thitsaworks.operation_portal.usecase.hub_operator.GetAllAnnouncement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class GetAllAnnouncementHandler extends HubOperatorUseCase<GetAllAnnouncement.Input, GetAllAnnouncement.Output>
    implements GetAllAnnouncement {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllAnnouncementHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.SUPERUSER,
                                                                    UserRoleType.ADMIN,
                                                                    UserRoleType.REPORTING,
                                                                    UserRoleType.OPERATION);

    private final AnnouncementQuery announcementQuery;

    public GetAllAnnouncementHandler(PrincipalCache principalCache,
                                     AnnouncementQuery announcementQuery) {

        super(PERMITTED_ROLES,
              principalCache);

        this.announcementQuery = announcementQuery;
    }

    @Override
    public GetAllAnnouncement.Output onExecute(GetAllAnnouncement.Input input) {

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

}
