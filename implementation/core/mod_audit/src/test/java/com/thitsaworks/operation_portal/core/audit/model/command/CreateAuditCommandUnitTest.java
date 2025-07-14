package com.thitsaworks.operation_portal.core.audit.model.command;

import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.infra.redis.RedisConfiguration;
import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.core.audit.AuditConfiguration;
import com.thitsaworks.operation_portal.core.audit.command.CreateAudit;
import com.thitsaworks.operation_portal.core.audit.model.BaseVaultSetUpTest;
import com.thitsaworks.operation_portal.core.audit.model.TestSettings;
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
        AuditConfiguration.class, TestSettings.class})
public class CreateAuditUnitTest extends BaseVaultSetUpTest {

    private static final Logger LOG = LoggerFactory.getLogger(CreateAuditUnitTest.class);

    @Autowired
    private CreateAudit createAudit;

    @Test
    public void test_createAuditSuccessfully() throws Exception {

        this.createAudit.execute(new CreateAudit.Input("Hikkk", new UserId(730781923773755392L),
                                                       new RealmId(730780506895921152L), "InputDummy", "Outputdummy"));
    }

}
