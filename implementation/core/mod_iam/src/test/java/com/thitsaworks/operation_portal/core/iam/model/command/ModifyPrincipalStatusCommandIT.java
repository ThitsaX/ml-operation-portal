package com.thitsaworks.operation_portal.core.iam.model.command;

import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.common.type.PrincipalStatus;
import com.thitsaworks.operation_portal.core.iam.IAMConfiguration;
import com.thitsaworks.operation_portal.core.iam.command.ModifyPrincipalStatusCommand;
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
public class ModifyPrincipalStatusCommandIT extends BaseVaultSetUpTest {

    private static final Logger LOGGER= LoggerFactory.getLogger(ModifyPrincipalStatusCommandIT.class);

    @Autowired
    private  ModifyPrincipalStatusCommand modifyPrincipalStatusCommand;

    @Test
    public void success() throws IAMException {
        var success= this.modifyPrincipalStatusCommand.execute(new ModifyPrincipalStatusCommand.Input(new PrincipalId(740553091875581952L),
                                                                                                      PrincipalStatus.INACTIVE));

        LOGGER.info("Id {} , boolean {}",success.principalId(),success.modified());
    }
}
