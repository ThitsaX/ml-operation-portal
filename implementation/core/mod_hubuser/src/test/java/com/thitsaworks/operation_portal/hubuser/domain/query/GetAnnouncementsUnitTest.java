package com.thitsaworks.operation_portal.hubuser.domain.query;

import com.thitsaworks.operation_portal.component.infra.hazelcast.HazelcastConfiguration;
import com.thitsaworks.operation_portal.core.hubuser.HubUserConfiguration;
import com.thitsaworks.operation_portal.core.hubuser.data.AnnouncementData;
import com.thitsaworks.operation_portal.core.hubuser.query.AnnouncementQuery;
import com.thitsaworks.operation_portal.hubuser.domain.TestSettings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HubUserConfiguration.class, TestSettings.class, HazelcastConfiguration.class})

public class GetAnnouncementsUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(GetAnnouncementsUnitTest.class);

    @Autowired
    private AnnouncementQuery announcementQuery;

    @Test
    public void test_getAnnouncementsSuccessfully() throws Exception {

        List<AnnouncementData> announcementDataList = this.announcementQuery.getAnnouncements();

        for (AnnouncementData announcementData : announcementDataList) {

            LOG.info("Announcements : [{}]", announcementData);

        }

    }

}
