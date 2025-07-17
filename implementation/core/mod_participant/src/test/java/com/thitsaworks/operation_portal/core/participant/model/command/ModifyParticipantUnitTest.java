package com.thitsaworks.operation_portal.core.participant.model.command;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.component.type.Mobile;
import com.thitsaworks.operation_portal.core.participant.command.ModifyParticipantCommand;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class ModifyParticipantUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyParticipantUnitTest.class);

    @Autowired
    private ModifyParticipantCommand modifyParticipantCommand;

    @Test
    public void test_modifyParticipantSuccessfully() throws Exception {

        this.modifyParticipantCommand.execute(
            new ModifyParticipantCommand.Input(new ParticipantId(390908599471210496L),
                                               "abc",
                                               "Ahlone Township, Yangon.",
                                               new Mobile("+959250661838"),
                                               null));
    }

}
