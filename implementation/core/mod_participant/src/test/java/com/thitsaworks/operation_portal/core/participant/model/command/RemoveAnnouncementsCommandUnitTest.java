package com.thitsaworks.operation_portal.core.participant.model.command;

import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.core.participant.command.RemoveAnnouncementsCommand;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class RemoveAnnouncementsCommandUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveAnnouncementsCommandUnitTest.class);

    @Autowired
    private RemoveAnnouncementsCommand removeAnnouncementsCommand;

    @Test
    public void test_removeAnnouncementsSuccessfully() {

        this.removeAnnouncementsCommand.execute(
                new RemoveAnnouncementsCommand.Input());
    }

}
