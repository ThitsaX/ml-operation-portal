package com.thitsaworks.operation_portal.audit.domain.command;

import com.thitsaworks.operation_portal.audit.AuditConfiguration;
import com.thitsaworks.operation_portal.audit.domain.persistence.MySqlDbSettings;
import com.thitsaworks.operation_portal.audit.identity.UserId;
import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.iam.identity.RealmId;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {AuditConfiguration.class, MySqlDbSettings.class})
public class CreateAuditUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(CreateAuditUnitTest.class);

    @Autowired
  private CreateAudit createAudit;

   @Test
    public void test_createAuditSuccessfully() throws Exception {

       this.createAudit.execute(new CreateAudit.Input(this.getClass().getSimpleName(), new UserId(392626097915330560L),
               new RealmId(390908599471210496L), "InputDummy", "Outputdummy"));
    }
}
