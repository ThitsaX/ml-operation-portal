package com.thitsaworks.operation_portal.core.iam.model.command;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.RoleId;
import com.thitsaworks.operation_portal.core.iam.IAMConfiguration;
import com.thitsaworks.operation_portal.core.iam.command.RevokeRoleActionByIdCommand;
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
    classes = {IAMConfiguration.class, TestSettings.class})
public class RevokeRoleActionByIdCommandIT extends BaseVaultSetUpTest {

    private static final Logger  LOGGER= LoggerFactory.getLogger(RevokeRoleActionByIdCommandIT.class);

    @Autowired
    private RevokeRoleActionByIdCommand revokeRoleActionByIdCommand;

    @Test
    public void  success() throws IAMException {
        var result = this.revokeRoleActionByIdCommand.execute(new RevokeRoleActionByIdCommand.Input(new RoleId(1L),
                                                                                                    new ActionId(740551758997073920L)));

        LOGGER.info("Boolean  {}",result.revoked());
    }
}
