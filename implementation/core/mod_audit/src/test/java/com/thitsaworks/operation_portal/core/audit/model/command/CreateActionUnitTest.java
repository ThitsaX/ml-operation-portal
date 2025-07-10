package com.thitsaworks.operation_portal.core.audit.model.command;

import com.thitsaworks.operation_portal.component.infra.redis.RedisConfiguration;
import com.thitsaworks.operation_portal.core.audit.AuditConfiguration;
import com.thitsaworks.operation_portal.core.audit.command.CreateAction;
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
public class CreateActionUnitTest extends BaseVaultSetUpTest {

    private static final Logger LOG = LoggerFactory.getLogger(CreateActionUnitTest.class);

    @Autowired
    private CreateAction createAction;

    @Test
    public void test_createActionSuccessfully() {

        CreateAction.Output output = this.createAction.execute(new CreateAction.Input("hiiiiii"));

        LOG.info("Action Id : " + output.actionId() + " , Created : " + output.created());
    }

}
