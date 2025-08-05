package com.thitsaworks.operation_portal.core.iam.model.command;

import com.thitsaworks.operation_portal.component.common.type.ActionCode;
import com.thitsaworks.operation_portal.core.iam.IAMConfiguration;
import com.thitsaworks.operation_portal.core.iam.command.GrantRoleActionCommand;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.iam.model.BaseVaultSetUpTest;
import com.thitsaworks.operation_portal.core.iam.model.TestSettings;
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
        IAMConfiguration.class, TestSettings.class})
public class GrantRoleActionCommandIT extends BaseVaultSetUpTest {

    private static final Logger LOG = LoggerFactory.getLogger(GrantRoleActionCommandIT.class);

    @Autowired
    private GrantRoleActionCommand grantRoleActionCommand;

    @Test
    public void success() throws IAMException {

        LOG.info("Grant Role Action : [{}]",
                 this.grantRoleActionCommand.execute(new GrantRoleActionCommand.Input("HUB-ADMIN",
                                                                                      new ActionCode(""))));
    }

}
