package com.thitsaworks.operation_portal.core.test_iam.IT.command;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.core.test_iam.IAMTestConfiguration;
import com.thitsaworks.operation_portal.core.test_iam.IT.BaseVaultSetUpTest;
import com.thitsaworks.operation_portal.core.test_iam.IT.TestSettings;
import com.thitsaworks.operation_portal.core.test_iam.command.BlockUserActionCommand;
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
public class BlockUserIAMActionCommandIT extends BaseVaultSetUpTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlockUserIAMActionCommandIT.class);

    @Autowired
    private BlockUserActionCommand blockUserActionCommand;

    @Test
    public void success() throws IAMException {

        var result = this.blockUserActionCommand.execute(new BlockUserActionCommand.Input(new UserId(739084478182305792L),
                                                                                          new ActionId(
                                                                                              738527399692447744L)));

        LOGGER.info("boolean {}", result.resultCode());
    }

}

