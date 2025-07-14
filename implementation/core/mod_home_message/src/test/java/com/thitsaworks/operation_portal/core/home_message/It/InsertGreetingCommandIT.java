package com.thitsaworks.operation_portal.core.home_message.It;

import com.thitsaworks.operation_portal.core.home_message.BaseVaultSetUpTest;
import com.thitsaworks.operation_portal.core.home_message.HomeMessageConfiguration;
import com.thitsaworks.operation_portal.core.home_message.TestSettings;
import com.thitsaworks.operation_portal.core.home_message.command.InsertGreetingCommand;
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
public class InsertGreetingCommandIT extends BaseVaultSetUpTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(InsertGreetingCommandIT.class);
    @Autowired
    private InsertGreetingCommand insertGreetingCommand;

    @Test
    public void testInsertGreetingCommand() {
        // Arrange
        String greetingTitle = "Welcooo  to Portal";
        String greetingDetail = "We are doooo to havehere!";

        // Act
        InsertGreetingCommand.Input input = new InsertGreetingCommand.Input(greetingTitle, greetingDetail);
        InsertGreetingCommand.Output output = insertGreetingCommand.execute(input);

        LOGGER.info("Greeting created: {}", output.created());
    }

}