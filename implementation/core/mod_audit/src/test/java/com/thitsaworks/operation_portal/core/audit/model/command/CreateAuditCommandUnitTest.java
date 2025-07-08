package com.thitsaworks.operation_portal.core.audit.model.command;

import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.infra.redis.RedisConfiguration;
import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.core.audit.AuditConfiguration;
import com.thitsaworks.operation_portal.core.audit.command.CreateAuditCommand;
import com.thitsaworks.operation_portal.core.audit.model.TestSettings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AuditConfiguration.class, TestSettings.class, RedisConfiguration.class})
public class CreateAuditCommandUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(CreateAuditCommandUnitTest.class);

    @Autowired
    private CreateAuditCommand createAuditCommand;

    @Test
    public void test_createAuditSuccessfully() throws Exception {

        this.createAuditCommand.execute(new CreateAuditCommand.Input(this.getClass()
                                                                         .getSimpleName(), new UserId(392626097915330560L),
                                                                     new RealmId(390908599471210496L), "InputDummy", "Outputdummy"));
    }

}
