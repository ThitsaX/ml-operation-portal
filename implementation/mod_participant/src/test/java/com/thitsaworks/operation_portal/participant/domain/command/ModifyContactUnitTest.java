package com.thitsaworks.operation_portal.participant.domain.command;

import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.component.type.Mobile;
import com.thitsaworks.operation_portal.participant.ParticipantConfiguration;
import com.thitsaworks.operation_portal.participant.domain.persistence.MySqlDbSettings;
import com.thitsaworks.operation_portal.participant.identity.ContactId;
import com.thitsaworks.operation_portal.participant.identity.ParticipantId;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {ParticipantConfiguration.class, MySqlDbSettings.class})
public class ModifyContactUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyContactUnitTest.class);

    @Autowired
    private ModifyContact modifyContact;

    @Test
    public void test_modifyContactSuccessfully() throws Exception {

        this.modifyContact.execute(
                new ModifyContact.Input(new ParticipantId(390906682871414784L),
                        new ContactId(390923899914715136L),
                        "Sandi Soe",
                        "General Manager",
                        new Email("info@thitsaworks.com"),
                        new Mobile("+959250661839")));
    }

}
