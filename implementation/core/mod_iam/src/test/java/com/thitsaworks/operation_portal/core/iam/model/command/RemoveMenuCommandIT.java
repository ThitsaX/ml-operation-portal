package com.thitsaworks.operation_portal.core.iam.model.command;

import com.thitsaworks.operation_portal.component.common.identifier.MenuId;
import com.thitsaworks.operation_portal.core.iam.IAMConfiguration;
import com.thitsaworks.operation_portal.core.iam.command.RemoveMenuCommand;
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
public class RemoveMenuCommandIT extends BaseVaultSetUpTest {
    private final Logger LOGGER = LoggerFactory.getLogger(RemoveMenuCommandIT.class);

    @Autowired
    private RemoveMenuCommand removeMenuCommand;

    @Test
    public void success() throws IAMException {
        var result = this.removeMenuCommand.execute(new RemoveMenuCommand.Input(new MenuId(1L)));

        LOGGER.info("Boolean {}",result.success());
    }

}
