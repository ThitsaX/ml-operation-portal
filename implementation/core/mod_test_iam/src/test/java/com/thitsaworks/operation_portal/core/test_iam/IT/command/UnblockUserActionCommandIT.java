package com.thitsaworks.operation_portal.core.test_iam.IT.command;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.core.test_iam.IAMTestConfiguration;
import com.thitsaworks.operation_portal.core.test_iam.IT.BaseVaultSetUpTest;
import com.thitsaworks.operation_portal.core.test_iam.IT.TestSettings;
import com.thitsaworks.operation_portal.core.test_iam.command.UnblockUserActionCommand;
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
public class UnblockUserActionCommandIT extends BaseVaultSetUpTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnblockUserActionCommandIT.class);

    @Autowired
    private UnblockUserActionCommand unblockUserActionCommand;

    @Test
    public void success() throws IAMException {
        var result = this.unblockUserActionCommand.execute(new UnblockUserActionCommand.Input(new UserId(739084478182305792L),
                                                                                              new ActionId(738527394302767104L)));
        LOGGER.info("Boolean {}",result.resultCode());
    }
}
