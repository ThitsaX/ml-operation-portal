package com.thitsaworks.operation_portal.core.iam.model.command;

import com.thitsaworks.operation_portal.component.common.type.ActionCode;
import com.thitsaworks.operation_portal.core.iam.IAMConfiguration;
import com.thitsaworks.operation_portal.core.iam.command.CreateOrUpdateActionCommand;
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
public class CreateOrUpdateIAMActionCommandIT extends BaseVaultSetUpTest {

    public static  final Logger LOGGER = LoggerFactory.getLogger(CreateOrUpdateIAMActionCommandIT.class);

    @Autowired
    private CreateOrUpdateActionCommand createOrUpdateActionCommand;

    @Test
    public void success(){
        var success= this.createOrUpdateActionCommand.execute(new CreateOrUpdateActionCommand.Input(new ActionCode("UpdateParticipantAmount"), "hi", "Automatically registered action for use case: UpdateParticipantAmountHandler"));

            LOGGER.info("ActionId {}",success.actionId());
    }
}
