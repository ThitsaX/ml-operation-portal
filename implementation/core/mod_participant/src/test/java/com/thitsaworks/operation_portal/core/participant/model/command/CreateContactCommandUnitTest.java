package com.thitsaworks.operation_portal.core.participant.model.command;

import com.thitsaworks.operation_portal.component.common.type.ContactType;
import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.component.type.Mobile;
import com.thitsaworks.operation_portal.core.participant.command.CreateContactCommand;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class CreateContactCommandUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(CreateContactCommandUnitTest.class);

    @Autowired
    private CreateContactCommand createContactCommand;

    @Test
    public void test_createContactSuccessfully() throws Exception {

        this.createContactCommand.execute(
                new CreateContactCommand.Input("Sandi",
                                               "Manager",
                                               new Email("info@thitsaworks.com"),
                                               new Mobile("+959400547258"),
                                               new ParticipantId(390906682871414784L), ContactType.BUSINESS));
    }

}
