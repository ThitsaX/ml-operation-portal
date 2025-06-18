package com.thitsaworks.operation_portal.usecase.participant;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.type.ParticipantInfo;
import com.thitsaworks.operation_portal.component.infra.hazelcast.HazelcastConfiguration;
import com.thitsaworks.operation_portal.usecase.ParticipantUseCaseConfiguration;
import com.thitsaworks.operation_portal.usecase.TestSettings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        ParticipantUseCaseConfiguration.class, TestSettings.class, HazelcastConfiguration.class})
public class GetAllOtherParticipantsUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllOtherParticipantsUnitTest.class);

    @Autowired
    private GetAllOtherParticipants getAllOtherParticipants;

    @Test
    public void test_getAllUserForParticipantSuccessfully() throws Exception {

        GetAllOtherParticipants.Output output = this.getAllOtherParticipants.execute(
                new GetAllOtherParticipants.Input(new ParticipantId(486547382195986432L)));

        for (ParticipantInfo participantInfo : output.getParticipantInfoList()) {
            LOG.info("Participant : [{}]", participantInfo);
        }

    }

}
