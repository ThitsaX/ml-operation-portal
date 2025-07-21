package com.thitsaworks.operation_portal.core.participant.model.command;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.infra.redis.RedisConfiguration;
import com.thitsaworks.operation_portal.component.common.type.Email;
import com.thitsaworks.operation_portal.core.participant.ParticipantConfiguration;
import com.thitsaworks.operation_portal.core.participant.command.CreateParticipantUserCommand;
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
public class CreateParticipantCommandUserUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(CreateParticipantCommandUserUnitTest.class);

    @Autowired
    private CreateParticipantUserCommand createParticipantUserCommand;

    @Test
    public void test_createUserSuccessfully() throws Exception {

        this.createParticipantUserCommand.execute(

            new CreateParticipantUserCommand.Input("ksh",
                                                   new Email("ksoohyun@email.com"),
                                                   new ParticipantId(486520066979930112L),
                                                   "kim",
                                                   "soohyun",
                                                   "actor"));

    }

}
