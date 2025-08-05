package com.thitsaworks.operation_portal.core.iam.model.command;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.MenuId;
import com.thitsaworks.operation_portal.component.common.identifier.RoleId;
import com.thitsaworks.operation_portal.core.iam.IAMConfiguration;
import com.thitsaworks.operation_portal.core.iam.command.GrantMenuActionByIdCommand;
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
public class GrantMenuActionByIdCommandIT extends BaseVaultSetUpTest {

    private static final Logger LOGGER= LoggerFactory.getLogger(GrantMenuActionByIdCommandIT.class);

    @Autowired
    private GrantMenuActionByIdCommand grantMenuActionByIdCommand;

    @Test
    public void success() throws IAMException {
        var success = this.grantMenuActionByIdCommand.execute(new GrantMenuActionByIdCommand.Input(new MenuId(2L),
                                                                                                   new ActionId(740551759743660032L)));

        LOGGER.info("Boolean {}",success.resultCode());
    }
}
