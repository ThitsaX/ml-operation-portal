package com.thitsaworks.operation_portal.core.home_message.It;

import com.thitsaworks.operation_portal.core.home_message.BaseVaultSetUpTest;
import com.thitsaworks.operation_portal.core.home_message.HomeMessageConfiguration;
import com.thitsaworks.operation_portal.core.home_message.TestSettings;
import com.thitsaworks.operation_portal.core.home_message.command.CreateGreetingMessageCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
    classes = {
        HomeMessageConfiguration.class, TestSettings.class})
public class CreateGreetingMessageMessageCommandIT extends BaseVaultSetUpTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateGreetingMessageMessageCommandIT.class);
    @Autowired
    private CreateGreetingMessageCommand createGreetingMessageCommand;

    @Test
    public void testInsertGreetingCommand() {
        // Arrange
        String greetingTitle = "Welcooo  to Portal";
        String greetingDetail = "We are doooo to havehere!";

        // Act
        CreateGreetingMessageCommand.Input input = new CreateGreetingMessageCommand.Input(greetingTitle, greetingDetail);
        CreateGreetingMessageCommand.Output output = createGreetingMessageCommand.execute(input);

        LOGGER.info("Greeting created: {}", output.created());
    }

}