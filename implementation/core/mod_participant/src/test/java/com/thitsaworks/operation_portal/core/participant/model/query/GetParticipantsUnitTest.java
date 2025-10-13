package com.thitsaworks.operation_portal.core.participant.model.query;

import com.thitsaworks.operation_portal.component.infra.redis.RedisConfiguration;
import com.thitsaworks.operation_portal.core.participant.ParticipantConfiguration;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantData;
import com.thitsaworks.operation_portal.core.participant.model.TestSettings;
import com.thitsaworks.operation_portal.core.participant.query.ParticipantQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ParticipantConfiguration.class, TestSettings.class, RedisConfiguration.class})
public class GetParticipantsUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(GetParticipantsUnitTest.class);

    @Autowired
    private ParticipantQuery participantQuery;

    @Test
    public void test_getParticipantsSuccessfully() {

        List<ParticipantData> participantDataList = this.participantQuery.getActiveParticipants();

        if (participantDataList.isEmpty()) {
            LOG.info("No record");
        }

        for (var participant : participantDataList) {
            LOG.info(participant.participantName() + " , " +
                                   participant.description() + " , " +
                                   participant.address() + " , " +
                                   participant.mobile()
                                              .getValue());
        }

    }

}
