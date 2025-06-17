package com.thitsaworks.operation_portal.core.audit.model.command;

import com.thitsaworks.operation_portal.component.infra.hazelcast.HazelcastConfiguration;
import com.thitsaworks.operation_portal.core.audit.AuditConfiguration;
import com.thitsaworks.operation_portal.core.audit.command.CreateAudit;
import com.thitsaworks.operation_portal.core.audit.model.TestSettings;
import com.thitsaworks.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.component.common.identifier.RealmId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AuditConfiguration.class, TestSettings.class, HazelcastConfiguration.class})
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
