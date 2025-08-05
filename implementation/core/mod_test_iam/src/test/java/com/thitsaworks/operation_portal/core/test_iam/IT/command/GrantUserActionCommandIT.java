package com.thitsaworks.operation_portal.core.test_iam.IT.command;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.core.test_iam.IAMTestConfiguration;
import com.thitsaworks.operation_portal.core.test_iam.IT.BaseVaultSetUpTest;
import com.thitsaworks.operation_portal.core.test_iam.IT.TestSettings;
import com.thitsaworks.operation_portal.core.test_iam.command.GrantUserActionCommand;
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
public class GrantUserActionCommandIT extends BaseVaultSetUpTest {

    private static final Logger LOG = LoggerFactory.getLogger(GrantUserActionCommandIT.class);

    @Autowired
    private GrantUserActionCommand grantUserActionCommand;

    @Test
    public void success() throws IAMException {

        LOG.info("Grant User Action : [{}]", this.grantUserActionCommand.execute(new GrantUserActionCommand.Input(new UserId(1111111111111111L),
                                                                                                                  new ActionId(739166424258293760L))));
    }

}
