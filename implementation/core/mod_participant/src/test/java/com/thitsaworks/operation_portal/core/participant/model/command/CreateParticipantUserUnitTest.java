package com.thitsaworks.operation_portal.core.participant.model.command;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.infra.redis.RedisConfiguration;
import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.core.participant.ParticipantConfiguration;
import com.thitsaworks.operation_portal.core.participant.command.CreateParticipantUser;
import com.thitsaworks.operation_portal.core.participant.model.TestSettings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ParticipantConfiguration.class, TestSettings.class, RedisConfiguration.class})
public class CreateParticipantUserUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(CreateParticipantUserUnitTest.class);

    @Autowired
    private CreateParticipantUser createParticipantUser;

    @Test
    public void test_createUserSuccessfully() throws Exception {

        this.createParticipantUser.execute(

            new CreateParticipantUser.Input("ksh",
                                            new Email("ksoohyun@email.com"),
                                            new ParticipantId(486520066979930112L),
                                            "kim",
                                            "soohyun",
                                            "actor"));

    }

}
