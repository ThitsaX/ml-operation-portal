package com.thitsaworks.dfsp_portal.participant.domain.command;

import com.thitsaworks.dfsp_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.dfsp_portal.component.type.Email;
import com.thitsaworks.dfsp_portal.participant.ParticipantConfiguration;
import com.thitsaworks.dfsp_portal.participant.domain.persistence.MySqlDbSettings;
import com.thitsaworks.dfsp_portal.participant.identity.ParticipantId;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {ParticipantConfiguration.class, MySqlDbSettings.class})
public class CreateParticipantUserUnitTest extends EnvAwareUnitTest {
    private static final Logger LOG = LoggerFactory.getLogger(CreateParticipantUserUnitTest.class);

    @Autowired
    private CreateParticipantUser createParticipantUser;

    @Test
    public void test_createUserSuccessfully() throws Exception {

        this.createParticipantUser.execute(

                new CreateParticipantUser.Input("ksh",new Email("ksoohyun@email.com"),new ParticipantId(392623203375808512L),"kim","soohyun","actor"));

             
    }


}
