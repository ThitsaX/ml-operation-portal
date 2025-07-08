package com.thitsaworks.operation_portal.core.home_message.It;

import com.thitsaworks.operation_portal.component.common.identifier.GreetingId;
import com.thitsaworks.operation_portal.core.home_message.BaseVaultSetUpTest;
import com.thitsaworks.operation_portal.core.home_message.HomeMessageConfiguration;
import com.thitsaworks.operation_portal.core.home_message.TestSettings;
import com.thitsaworks.operation_portal.core.home_message.command.UpdateGreetingCommand;
import com.thitsaworks.operation_portal.core.home_message.exception.GreetingException;
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
public class UpdateGreetingCommandIT extends BaseVaultSetUpTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateGreetingCommandIT.class);

    @Autowired
    private UpdateGreetingCommand updateGreetingCommand;

    @Test
    public void testUpdateGreetingCommand() throws GreetingException {

        var input = new UpdateGreetingCommand.Input(new GreetingId(730159064823083008L),
                                                                            "Updated Greeting Title",
                                                                            "Updated Greeting Detail");

        // When
        var output = this.updateGreetingCommand.execute(input);


        LOGGER.info("Greeting updated successfully with ID: {}", output.greetingId());
    }

}
