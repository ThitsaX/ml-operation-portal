package com.thitsaworks.operation_portal.core.test_iam.IT.command;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.core.test_iam.IAMTestConfiguration;
import com.thitsaworks.operation_portal.core.test_iam.IT.BaseVaultSetUpTest;
import com.thitsaworks.operation_portal.core.test_iam.IT.TestSettings;
import com.thitsaworks.operation_portal.core.test_iam.command.RevokeUserActionCommand;
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
public class RevokeUserActionCommandIT extends BaseVaultSetUpTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RevokeUserActionCommandIT.class);

    @Autowired
    private RevokeUserActionCommand revokeUserActionCommand;

    @Test
    public void success() throws IAMException {
        var result= this.revokeUserActionCommand.execute(new RevokeUserActionCommand.Input(new UserId(739114775909216256L),
                                                                                           new ActionId(738527399885385728L)));

        LOGGER.info("Boolean {}",result.revoked());
    }
}
