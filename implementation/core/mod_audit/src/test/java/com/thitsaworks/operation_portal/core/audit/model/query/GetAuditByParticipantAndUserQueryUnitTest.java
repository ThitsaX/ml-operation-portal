package com.thitsaworks.operation_portal.core.audit.model.query;

import com.thitsaworks.operation_portal.component.infra.hazelcast.HazelcastConfiguration;
import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.core.audit.AuditConfiguration;
import com.thitsaworks.operation_portal.core.audit.exception.UserNotFoundException;
import com.thitsaworks.operation_portal.core.audit.model.TestSettings;
import com.thitsaworks.operation_portal.core.audit.query.GetAuditByParticipantAndUserQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AuditConfiguration.class, TestSettings.class, HazelcastConfiguration.class})
public class GetAuditByParticipantAndUserQueryUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(GetAuditByParticipantAndUserQueryUnitTest.class);

    @Autowired
    private GetAuditByParticipantAndUserQuery getAuditByParticipantAndUserQuery;

    @Test
    public void test_getAuditsSuccessfully() throws UserNotFoundException {

        GetAuditByParticipantAndUserQuery.Output output = this.getAuditByParticipantAndUserQuery.execute(new GetAuditByParticipantAndUserQuery.Input(null, null, null, null));

        if (output.getAuditInfoList() != null) {
            for (var obj : output.getAuditInfoList())
                LOG.info(obj.getUserName() + " , " + obj.getParticipantName() + "," + obj.getActionName());
        } else {
            LOG.info("No record");
        }

    }

}
