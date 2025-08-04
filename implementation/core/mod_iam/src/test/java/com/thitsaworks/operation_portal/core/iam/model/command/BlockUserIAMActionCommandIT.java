package com.thitsaworks.operation_portal.core.iam.model.command;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.core.iam.IAMConfiguration;
import com.thitsaworks.operation_portal.core.iam.command.BlockPrincipalActionCommand;
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
public class BlockUserIAMActionCommandIT extends BaseVaultSetUpTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlockUserIAMActionCommandIT.class);

    @Autowired
    private BlockPrincipalActionCommand blockPrincipalActionCommand;

    @Test
    public void success() throws IAMException {

        var result = this.blockPrincipalActionCommand.execute(new BlockPrincipalActionCommand.Input(new PrincipalId(1111111111111111L),
                                                                                          new ActionId(
                                                                                              737672397218869248L)));

        LOGGER.info("boolean {}", result.resultCode());
    }

}

