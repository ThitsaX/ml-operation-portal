package com.thitsaworks.operation_portal.dfsp_portal.audit.domain.query;

import com.thitsaworks.operation_portal.dfsp_portal.audit.AuditConfiguration;
import com.thitsaworks.operation_portal.dfsp_portal.audit.domain.persistence.MySqlDbSettings;
import com.thitsaworks.operation_portal.dfsp_portal.audit.exception.UserNotFoundException;
import com.thitsaworks.operation_portal.dfsp_portal.audit.query.GetAudit;
import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {AuditConfiguration.class, MySqlDbSettings.class})
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
