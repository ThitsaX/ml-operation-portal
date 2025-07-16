package com.thitsaworks.operation_portal.core.participant.model.command;

import com.thitsaworks.operation_portal.component.common.type.DfspCode;
import com.thitsaworks.operation_portal.component.infra.redis.RedisConfiguration;
import com.thitsaworks.operation_portal.component.type.Mobile;
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

        this.createParticipantCommand.execute(new CreateParticipantCommand.Input("Vision Fund",
                                                                                 new DfspCode("abcdefg"), "abc",
                                                                                 "Yankin Township, Yangon.",
                                                                                 new Mobile("+959250661838"),
                                                                                 "image",
                                                                                 null,
                                                                                 null, null));

    }

}
