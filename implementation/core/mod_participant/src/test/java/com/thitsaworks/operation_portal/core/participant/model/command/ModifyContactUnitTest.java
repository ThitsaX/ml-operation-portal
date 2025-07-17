package com.thitsaworks.operation_portal.core.participant.model.command;

import com.thitsaworks.operation_portal.component.common.identifier.ContactId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.type.ContactType;
import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.component.type.Mobile;
import com.thitsaworks.operation_portal.core.participant.command.ModifyContactCommand;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class ModifyContactUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyContactUnitTest.class);

    @Autowired
    private ModifyContactCommand modifyContactCommand;

    @Test
    public void test_modifyContactSuccessfully() throws Exception {

        this.modifyContactCommand.execute(
                new ModifyContactCommand.Input(new ParticipantId(390906682871414784L),
                                               new ContactId(390923899914715136L),
                                               "Sandi Soe",
                                               "General Manager",
                                               new Email("info@thitsaworks.com"),
                                               new Mobile("+959250661839"), ContactType.TECHNICAL));
    }

}
