package com.thitsaworks.operation_portal.core.participant.model.query;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.infra.redis.RedisConfiguration;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.core.participant.ParticipantConfiguration;
import com.thitsaworks.operation_portal.core.participant.data.UserData;
import com.thitsaworks.operation_portal.core.participant.model.TestSettings;
import com.thitsaworks.operation_portal.core.participant.query.UserQuery;
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
public class GetUserUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(GetUserUnitTest.class);

    @Autowired
    private UserQuery userQuery;

    @Test
    public void test_getUsersSuccessfully() throws DomainException {

        List<UserData> userDataList =
                this.userQuery.getUsers(new ParticipantId(486547382195986432L));

        if (userDataList != null) {

            LOG.info("Participant Users  : [{}]", userDataList);
        } else {
            LOG.info("No record");
        }

    }

    @Test
    public void test_getUserSuccessfully() throws DomainException {

        UserData userData =
                this.userQuery.get(new UserId(486549751625580544L));

        if (userData != null) {

            LOG.info("Participant User  : [{}]", userData);
        } else {
            LOG.info("No record");
        }

    }

}
