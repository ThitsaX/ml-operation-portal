package com.thitsaworks.operation_portal.usecase.hub_operator.It;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.HubOperatorUseCaseConfiguration;
import com.thitsaworks.operation_portal.usecase.hub_operator.BaseVaultSetUpTest;
import com.thitsaworks.operation_portal.usecase.hub_operator.CreateNewAnnouncement;
import com.thitsaworks.operation_portal.usecase.hub_operator.TestSettings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {
                HubOperatorUseCaseConfiguration.class, TestSettings.class})
public class CreateNewAnnouncementIT extends BaseVaultSetUpTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateNewAnnouncementIT.class);

    @Autowired
    private CreateNewAnnouncement createNewAnnouncement;

    @Test
    public void success() throws DomainException {

        var input = new CreateNewAnnouncement.Input("Announcement am",
                "Announcement i", Instant.now());

        var output = this.createNewAnnouncement.execute(input);

        LOGGER.info("Boolean {}",output.created());
    }
}

