package com.thitsaworks.operation_portal.hubuser.domain.command;

import com.thitsaworks.operation_portal.core.hubuser.HubUserConfiguration;
import com.thitsaworks.operation_portal.core.hubuser.command.CreateGreetingMessageCommand;
import com.thitsaworks.operation_portal.core.hubuser.exception.HubUserException;
import com.thitsaworks.operation_portal.hubuser.domain.TestSettings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HubUserConfiguration.class, TestSettings.class})
public class CreateGreetingMessageCommandIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateGreetingMessageCommandIT.class);

    @Autowired
    private CreateGreetingMessageCommand createGreetingMessageCommand;

    @Test
    public void testExecute() throws HubUserException {
        var input = new CreateGreetingMessageCommand.Input(
            "Welcome to Operation Portal",
            "We are glad to have you here!",
            Instant.now()
        );

        var output = createGreetingMessageCommand.execute(input);

        LOGGER.info("Output: {}", output);
    }

}

