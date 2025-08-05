package com.thitsaworks.operation_portal.core.iam.model.command;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.RoleId;
import com.thitsaworks.operation_portal.core.iam.IAMConfiguration;
import com.thitsaworks.operation_portal.core.iam.command.GrantRoleActionByIdCommand;
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
public class GrantRokeActionByIdCommandIT extends BaseVaultSetUpTest {

    private static final Logger LOGGER= LoggerFactory.getLogger(GrantRokeActionByIdCommandIT.class);

    @Autowired
    private GrantRoleActionByIdCommand grantRoleActionByIdCommand;

    @Test
    public void success() throws IAMException {
        var success = this.grantRoleActionByIdCommand.execute(new GrantRoleActionByIdCommand.Input(new RoleId(2L),new ActionId(740551768270680064L)));

        LOGGER.info("Boolean {}",success.resultCode());
    }
}
