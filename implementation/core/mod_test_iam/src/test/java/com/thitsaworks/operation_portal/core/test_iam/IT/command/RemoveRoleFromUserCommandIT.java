package com.thitsaworks.operation_portal.core.test_iam.IT.command;

import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.identifier.iamtestid.RoleId;
import com.thitsaworks.operation_portal.core.test_iam.IAMTestConfiguration;
import com.thitsaworks.operation_portal.core.test_iam.IT.BaseVaultSetUpTest;
import com.thitsaworks.operation_portal.core.test_iam.IT.TestSettings;
import com.thitsaworks.operation_portal.core.test_iam.command.RemoveRoleFromUserCommand;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;
import org.checkerframework.checker.units.qual.A;
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
public class RemoveRoleFromUserCommandIT extends BaseVaultSetUpTest {

    private final Logger LOGGER= LoggerFactory.getLogger(RemoveRoleFromUserCommandIT.class);

    @Autowired
    private RemoveRoleFromUserCommand removeRoleFromUserCommand;

    @Test
    public void  success() throws IAMException {
        var result= this.removeRoleFromUserCommand.execute(new RemoveRoleFromUserCommand.Input(new UserId(739084478182305792L),
                                                                                               new RoleId(739079062484398080L)));
        LOGGER.info("Boolean {}",result.removed());
    }
}
