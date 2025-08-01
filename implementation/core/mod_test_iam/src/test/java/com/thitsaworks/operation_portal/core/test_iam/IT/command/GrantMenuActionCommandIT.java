package com.thitsaworks.operation_portal.core.test_iam.IT.command;

import com.thitsaworks.operation_portal.component.common.type.iamtesttype.ActionCode;
import com.thitsaworks.operation_portal.core.test_iam.IAMTestConfiguration;
import com.thitsaworks.operation_portal.core.test_iam.IT.BaseVaultSetUpTest;
import com.thitsaworks.operation_portal.core.test_iam.IT.TestSettings;
import com.thitsaworks.operation_portal.core.test_iam.command.GrantMenuActionCommand;
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
public class GrantMenuActionCommandIT extends BaseVaultSetUpTest {

    private final Logger LOGGER = LoggerFactory.getLogger(GrantMenuActionCommandIT.class);

    @Autowired
    private GrantMenuActionCommand grantMenuActionCommand;

    @Test
    public void success() throws IAMException {
        var result = this.grantMenuActionCommand.execute(new GrantMenuActionCommand.Input("hi",
                                                                                         new ActionCode("hi")));

        LOGGER.info("Boolean{}",result.resultCode());
    }
}
