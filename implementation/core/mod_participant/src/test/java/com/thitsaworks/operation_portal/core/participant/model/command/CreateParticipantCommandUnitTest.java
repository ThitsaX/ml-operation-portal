package com.thitsaworks.operation_portal.core.participant.model.command;

import com.thitsaworks.operation_portal.component.common.type.ParticipantName;
import com.thitsaworks.operation_portal.component.common.type.ParticipantStatus;
import com.thitsaworks.operation_portal.component.infra.redis.RedisConfiguration;
import com.thitsaworks.operation_portal.component.common.type.Mobile;
import com.thitsaworks.operation_portal.core.participant.ParticipantConfiguration;
import com.thitsaworks.operation_portal.core.participant.command.CreateParticipantCommand;
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
public class CreateParticipantCommandUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(CreateParticipantCommandUnitTest.class);

    @Autowired
    private CreateParticipantCommand createParticipantCommand;

    @Test
    public void test_createParticipantSuccessfully() throws Exception {

        this.createParticipantCommand.execute(new CreateParticipantCommand.Input(1, new ParticipantName("abcdefg"),
                                                                                 "Vision Fund",
                                                                                 "address",
                                                                                 new Mobile("+959250661838"),
                                                                                 ParticipantStatus.ACTIVE,
                                                                                 null,
                                                                                 null));

    }

}
