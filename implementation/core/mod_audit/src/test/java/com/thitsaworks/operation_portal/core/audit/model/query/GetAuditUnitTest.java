package com.thitsaworks.operation_portal.core.audit.model.query;

import com.thitsaworks.operation_portal.component.infra.hazelcast.HazelcastConfiguration;
import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.core.audit.AuditConfiguration;
import com.thitsaworks.operation_portal.core.audit.exception.UserNotFoundException;
import com.thitsaworks.operation_portal.core.audit.model.TestSettings;
import com.thitsaworks.operation_portal.core.audit.query.GetAudit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AuditConfiguration.class, TestSettings.class, HazelcastConfiguration.class})
public class GetAuditUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(GetAuditUnitTest.class);

    @Autowired
    private GetAudit getAudit;

    @Test
    public void test_getAuditsSuccessfully() throws UserNotFoundException {

        GetAudit.Output output = this.getAudit.execute(new GetAudit.Input(null, null, null, null));

        if (output.getAuditInfoList() != null) {
            for (var obj : output.getAuditInfoList())
                LOG.info(obj.getUserName() + " , " + obj.getParticipantName() + "," + obj.getActionName());
        } else {
            LOG.info("No record");
        }

    }

}
