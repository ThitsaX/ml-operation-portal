package com.thitsaworks.operation_portal.core.test_iam.IT.command;

import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.identifier.iamtestid.RoleId;
import com.thitsaworks.operation_portal.core.test_iam.IAMTestConfiguration;
import com.thitsaworks.operation_portal.core.test_iam.IT.BaseVaultSetUpTest;
import com.thitsaworks.operation_portal.core.test_iam.IT.TestSettings;
import com.thitsaworks.operation_portal.core.test_iam.command.AssignRoleToUserCommand;
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
    classes = {IAMTestConfiguration.class, TestSettings.class})
public class AssignRoleToUserCommandIT extends BaseVaultSetUpTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(AssignRoleToUserCommandIT.class);

    @Autowired
    private AssignRoleToUserCommand assignRoleToUserCommand;

    @Test
    public void success() throws IAMException {
        var result = this.assignRoleToUserCommand.execute(new AssignRoleToUserCommand.Input(new UserId(1111111111111111L),
                                                                                       new RoleId(738348341732192256L)));

        LOGGER.info("Role assigned successfully: {}", result.userRoleId());
    }


}
