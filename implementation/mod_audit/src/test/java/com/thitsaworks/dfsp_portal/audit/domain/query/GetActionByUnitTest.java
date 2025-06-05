package com.thitsaworks.dfsp_portal.audit.domain.query;

import com.thitsaworks.dfsp_portal.audit.AuditConfiguration;
import com.thitsaworks.dfsp_portal.audit.domain.persistence.MySqlDbSettings;
import com.thitsaworks.dfsp_portal.audit.identity.UserId;
import com.thitsaworks.dfsp_portal.audit.query.GetActionBy;
import com.thitsaworks.dfsp_portal.component.test.EnvAwareUnitTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {AuditConfiguration.class, MySqlDbSettings.class})
public class GetActionByUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(GetActionByUnitTest.class);

    @Autowired
    private GetActionBy getActionBy;

    @Test
    public void test_getActionBySuccessfully() throws Exception {

        GetActionBy.Output output = this.getActionBy.execute(new GetActionBy.Input(new UserId(392628367895068672L)));

        LOG.info(output.getUserData().getUserId().toString() + "," + output.getUserData().getParticipantId());

    }

}
