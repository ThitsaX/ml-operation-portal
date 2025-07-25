package com.thitsaworks.operation_portal.hubuser.domain.command;

import com.thitsaworks.operation_portal.component.common.identifier.GreetingId;
import com.thitsaworks.operation_portal.core.hubuser.HubUserConfiguration;
import com.thitsaworks.operation_portal.core.hubuser.command.UpdateGreetingCommand;
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
public class UpdateGreetinCommandIT {

    private static final Logger LOGGER= LoggerFactory.getLogger(UpdateGreetinCommandIT.class);

    @Autowired
    private UpdateGreetingCommand updateGreetingCommand;

    @Test
    public void success() throws HubUserException {
        var input = new UpdateGreetingCommand.Input(
            new GreetingId(1L),
            "Updated Greeting Title",
            "Updated Greeting Detail",
            false,
            Instant.now()
        );
        var output = updateGreetingCommand.execute(input);

        LOGGER.info("Output: {}", output.greetingId());
    }

}


