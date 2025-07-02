package com.thitsaworks.operation_portal.core.participant.model.command;

import com.thitsaworks.operation_portal.component.common.type.ContactType;
import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.component.type.Mobile;
import com.thitsaworks.operation_portal.core.participant.command.CreateContact;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

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
                                        new ParticipantId(390906682871414784L), ContactType.BUSINESS));
    }

}
