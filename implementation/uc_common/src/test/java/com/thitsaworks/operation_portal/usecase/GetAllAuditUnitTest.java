package com.thitsaworks.operation_portal.usecase;

import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.datasource.persistence.MySqlDbSettings;
import com.thitsaworks.operation_portal.iam.identity.RealmId;
import com.thitsaworks.operation_portal.usecase.common.GetAllAudit;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {CommonUseCaseConfiguration.class, MySqlDbSettings.class})
public class GetAllAuditUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllAuditUnitTest.class);

    @Autowired
    private GetAllAudit getAllAudit;

    @Test
    public void test_getAllAuditForParticipantSuccessfully() throws Exception {

        var output = this.getAllAudit.execute(
                new GetAllAudit.Input(new RealmId(390906682871414784L), null, null, null));

        if (output.getAuditInfoList() != null) {
            for (var obj : output.getAuditInfoList()) {
                LOG.info("ParticipantUser Name : " + obj.getUserName() +
                        " , Participant :  " + obj.getParticipantName() +
                        " , Action : " + obj.getActionName() +
                        " , Action Date : " + obj.getActionDate());
            }
        } else {
            LOG.info("No record");
        }
    }

}
