package com.thitsaworks.operation_portal.audit.domain.command;

import com.thitsaworks.operation_portal.audit.AuditConfiguration;
import com.thitsaworks.operation_portal.audit.domain.persistence.MySqlDbSettings;
import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {AuditConfiguration.class, MySqlDbSettings.class})
public class CreateActionUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(CreateActionUnitTest.class);

    @Autowired
    private CreateAction createAction;

    @Test
    public void test_createActionSuccessfully() {

        CreateAction.Output output = this.createAction.execute(new CreateAction.Input(this.getClass().getSimpleName()));

        LOG.info("Action Id : " + output.getActionId() + " , Created : " + output.isCreated());
    }

}
