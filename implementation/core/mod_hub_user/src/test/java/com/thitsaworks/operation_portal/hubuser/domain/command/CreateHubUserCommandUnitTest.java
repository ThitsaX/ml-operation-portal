package com.thitsaworks.operation_portal.hubuser.domain.command;

import com.thitsaworks.operation_portal.component.common.type.Email;
import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.core.hubuser.command.CreateHubUserCommand;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class CreateHubUserCommandUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(CreateHubUserCommandUnitTest.class);

    @Autowired
    private CreateHubUserCommand createHubUserCommand;

    @Test
    public void test_createHubUserSuccessfully() throws Exception {

        this.createHubUserCommand.execute(
                new CreateHubUserCommand.Input("Ingyin", new Email("Ingyin@thitsaworks.com"), "Ingyin", "thandar", "BA"));
    }

}
