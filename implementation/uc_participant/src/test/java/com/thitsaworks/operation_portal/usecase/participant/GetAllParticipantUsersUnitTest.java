package com.thitsaworks.operation_portal.usecase.participant;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.infra.redis.RedisConfiguration;
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
@ContextConfiguration(
    classes = {
        ParticipantUseCaseConfiguration.class, TestSettings.class, RedisConfiguration.class})
public class GetAllParticipantUsersUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllParticipantUsersUnitTest.class);

    @Autowired
    private GetAllParticipantUser getAllParticipantUser;

    @Test
    public void test_getAllUserForParticipantSuccessfully() throws Exception {

        GetAllParticipantUser.Output output = this.getAllParticipantUser.execute(
            new GetAllParticipantUser.Input(new ParticipantId(486547382195986432L)));

        for (GetAllParticipantUser.UserInfo userInfo : output.userInfoList()) {
            LOG.info("User : [{}]", userInfo);
        }

    }

}
