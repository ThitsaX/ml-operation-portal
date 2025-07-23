package com.thitsaworks.operation_portal.hubuser.domain.command;

import com.thitsaworks.operation_portal.core.hubuser.HubUserConfiguration;
import com.thitsaworks.operation_portal.core.hubuser.command.RemoveGreetingCommand;
import com.thitsaworks.operation_portal.hubuser.domain.TestSettings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HubUserConfiguration.class, TestSettings.class})
public class RemoveGreetingCommandIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoveGreetingCommandIT.class);

    @Autowired
    private RemoveGreetingCommand removeGreetingCommand;

    @Test
    public void success(){
        var input = new RemoveGreetingCommand.Input();
        var output = removeGreetingCommand.execute(input);
        LOGGER.info("Output: {}", output.removed());
    }
}

