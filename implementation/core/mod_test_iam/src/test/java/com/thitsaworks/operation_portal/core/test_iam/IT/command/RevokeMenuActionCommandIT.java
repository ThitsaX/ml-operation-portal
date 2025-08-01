package com.thitsaworks.operation_portal.core.test_iam.IT.command;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.iamtestid.MenuId;
import com.thitsaworks.operation_portal.core.test_iam.IAMTestConfiguration;
import com.thitsaworks.operation_portal.core.test_iam.IT.BaseVaultSetUpTest;
import com.thitsaworks.operation_portal.core.test_iam.IT.TestSettings;
import com.thitsaworks.operation_portal.core.test_iam.command.RevokeMenuActionCommand;
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
public class RevokeMenuActionCommandIT extends BaseVaultSetUpTest {

    private final Logger LOGGER = LoggerFactory.getLogger(RevokeMenuActionCommandIT.class);

    @Autowired
    private RevokeMenuActionCommand revokeMenuActionCommand;

    @Test
    public void success() throws IAMException {
        var result = this.revokeMenuActionCommand.execute(new RevokeMenuActionCommand.Input(new MenuId(22L),
                                                                                            new ActionId(333L)));

        LOGGER.info("Boolean{}",result.revoked());
    }
}
