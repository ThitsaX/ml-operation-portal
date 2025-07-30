package com.thitsaworks.operation_portal.core.test_iam.IT.command;

import com.thitsaworks.operation_portal.core.test_iam.IAMTestConfiguration;
import com.thitsaworks.operation_portal.core.test_iam.IT.BaseVaultSetUpTest;
import com.thitsaworks.operation_portal.core.test_iam.IT.TestSettings;
import com.thitsaworks.operation_portal.core.test_iam.command.CreateRoleCommand;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;
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
        IAMTestConfiguration.class, TestSettings.class})
public class CreateRoleCommandIT extends BaseVaultSetUpTest {

    private static final Logger LOG = LoggerFactory.getLogger(CreateRoleCommandIT.class);

    @Autowired
    private CreateRoleCommand createRoleCommand;

    @Test
    public void success() throws IAMException {

        LOG.info("Role : [{}]", this.createRoleCommand.execute(new CreateRoleCommand.Input("OPERATION")).roleId());
    }

}
