package com.thitsaworks.operation_portal.core.test_iam.IT.command;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.iamtestid.RoleId;
import com.thitsaworks.operation_portal.core.test_iam.IAMTestConfiguration;
import com.thitsaworks.operation_portal.core.test_iam.IT.BaseVaultSetUpTest;
import com.thitsaworks.operation_portal.core.test_iam.IT.TestSettings;
import com.thitsaworks.operation_portal.core.test_iam.command.GrantRoleActionCommand;
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
public class GrantRoleActionCommandIT extends BaseVaultSetUpTest {

    private static final Logger LOG = LoggerFactory.getLogger(GrantRoleActionCommandIT.class);

    @Autowired
    private GrantRoleActionCommand grantRoleActionCommand;

    @Test
    public void success() throws IAMException {

        LOG.info("Grant Role Action : [{}]", this.grantRoleActionCommand.execute(new GrantRoleActionCommand.Input(new RoleId(739079062484398080L), new ActionId(739084478182305792L))));
    }
}
