package com.thitsaworks.operation_portal.core.participant.model.query;

import com.thitsaworks.operation_portal.component.infra.redis.RedisConfiguration;
import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.core.participant.ParticipantConfiguration;
import com.thitsaworks.operation_portal.core.participant.model.TestSettings;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ParticipantConfiguration.class, TestSettings.class, RedisConfiguration.class})
public class GetUserIdsUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(GetUserIdsUnitTest.class);

//    @Autowired
//    private GetUser getUserIds;
//
//    @Test
//    public void test_getUserIdsSuccessfully() {
//
//        GetUserIds.Output output =
//                this.getUserIds.execute(new GetUserIds.Input(new ParticipantId(392623203375808512L)));
//        LOG.info("Size: " + output.getParticipantUserIds().size());
//
//        for (ParticipantUserId id : output.getParticipantUserIds()) {
//
//            LOG.info("id: " + id);
//        }
//
//    }

}
