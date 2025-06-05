package com.thitsaworks.dfsp_portal.participant.domain.command;

import com.thitsaworks.dfsp_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.dfsp_portal.component.type.Email;
import com.thitsaworks.dfsp_portal.component.type.Mobile;
import com.thitsaworks.dfsp_portal.participant.ParticipantConfiguration;
import com.thitsaworks.dfsp_portal.participant.domain.persistence.MySqlDbSettings;
import com.thitsaworks.dfsp_portal.participant.identity.ParticipantId;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(classes = {ParticipantConfiguration.class, MySqlDbSettings.class})
public class CreateContactUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(CreateContactUnitTest.class);

    @Autowired
    private CreateContact createContact;

    @Test
    public void test_createContactSuccessfully() throws Exception {

        this.createContact.execute(
                new CreateContact.Input("Sandi",
                        "Manager",
                        new Email("info@thitsaworks.com"),
                        new Mobile("+959400547258"),
                        new ParticipantId(390906682871414784L),
                        "Business"));
    }

}
