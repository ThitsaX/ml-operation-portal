package com.thitsaworks.operation_portal.dfsp_portal.participant.domain.command;

import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.dfsp_portal.participant.identity.ParticipantId;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

public class CreateParticipantUserUnitTest extends EnvAwareUnitTest {
    private static final Logger LOG = LoggerFactory.getLogger(CreateParticipantUserUnitTest.class);

    @Autowired
    private CreateParticipantUser createParticipantUser;

    @Test
    public void test_createUserSuccessfully() throws Exception {

        this.createParticipantUser.execute(

                new CreateParticipantUser.Input("ksh", new Email("ksoohyun@email.com"), new ParticipantId(392623203375808512L), "kim", "soohyun", "actor"));

             
    }


}
