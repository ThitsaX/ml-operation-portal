package com.thitsaworks.operation_portal.core.test_iam.IT.command;

import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.type.PrincipalStatus;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.core.test_iam.IAMTestConfiguration;
import com.thitsaworks.operation_portal.core.test_iam.IT.BaseVaultSetUpTest;
import com.thitsaworks.operation_portal.core.test_iam.IT.TestSettings;
import com.thitsaworks.operation_portal.core.test_iam.command.ModifyUserCommand;
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
public class ModifyUserCommandIT extends BaseVaultSetUpTest {

    private final Logger LOGGER= LoggerFactory.getLogger(ModifyUserCommandIT.class);

    @Autowired
    private ModifyUserCommand modifyUserCommand;

    @Test
    public void success() throws IAMException {
        var result = this.modifyUserCommand.execute(new ModifyUserCommand.Input(new UserId(739084478182305792L),
                                                                                UserRoleType.valueOf("OPERATION"),
                                                                                PrincipalStatus.ACTIVE));

        LOGGER.info("Boolean {}",result.modified());
    }
}
