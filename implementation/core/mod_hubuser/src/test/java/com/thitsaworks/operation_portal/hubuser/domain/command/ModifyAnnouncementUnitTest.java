package com.thitsaworks.operation_portal.hubuser.domain.command;

import com.thitsaworks.operation_portal.component.common.identifier.AnnouncementId;
import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.core.hubuser.command.ModifyAnnouncement;
import com.thitsaworks.operation_portal.core.hubuser.exception.AlreadyAnnouncedException;
import com.thitsaworks.operation_portal.core.hubuser.exception.AnnouncementNotFoundException;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.Calendar;

public class ModifyAnnouncementUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyAnnouncementUnitTest.class);

    @Autowired
    private ModifyAnnouncement modifyAnnouncement;

    @Test
    public void test_modifyAnnouncementSuccessfully() throws AnnouncementNotFoundException, AlreadyAnnouncedException {

        Calendar c1 = Calendar.getInstance();
        c1.set(Calendar.MONTH, Calendar.MARCH);
        c1.set(Calendar.DATE, 1);
        c1.set(Calendar.YEAR, 2023);

        Instant announcementDate = Instant.ofEpochMilli(c1.getTimeInMillis());
        this.modifyAnnouncement.execute(
                new ModifyAnnouncement.Input(new AnnouncementId(410480760326225920L), "March Announcement",
                        "March Announcement blah blah blah", null, false));
    }

}
