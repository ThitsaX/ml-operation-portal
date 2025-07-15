package com.thitsaworks.operation_portal.core.audit.model.command;

import com.thitsaworks.operation_portal.core.audit.AuditConfiguration;
import com.thitsaworks.operation_portal.core.audit.command.CreateActionCommand;
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
public class CreateActionIT extends BaseVaultSetUpTest {

    private static final Logger LOG = LoggerFactory.getLogger(CreateActionIT.class);

    @Autowired
    private CreateActionCommand createActionCommand;

    @Test
    public void test_createActionSuccessfully() {

        var output = this.createActionCommand.execute(new CreateActionCommand.Input("iiii"));

        LOG.info("Action Id : " + output.actionId() + " , Created : " + output.created());
    }

}
