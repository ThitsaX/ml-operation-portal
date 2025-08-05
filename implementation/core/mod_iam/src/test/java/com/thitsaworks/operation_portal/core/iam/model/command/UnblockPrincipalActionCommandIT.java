package com.thitsaworks.operation_portal.core.iam.model.command;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.core.iam.IAMConfiguration;
import com.thitsaworks.operation_portal.core.iam.command.UnblockPrincipalActionCommand;
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
public class UnblockPrincipalActionCommandIT extends BaseVaultSetUpTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(UnblockPrincipalActionCommandIT.class);

    @Autowired
    private UnblockPrincipalActionCommand unblockUserActionCommand;

    @Test
    public void success() throws IAMException {
        var result = this.unblockUserActionCommand.execute(new UnblockPrincipalActionCommand.Input(new PrincipalId(1111111111111111L),
                                                                                              new ActionId(740551758481174528L)));
        LOGGER.info("Boolean {}",result.resultCode());
    }
}
