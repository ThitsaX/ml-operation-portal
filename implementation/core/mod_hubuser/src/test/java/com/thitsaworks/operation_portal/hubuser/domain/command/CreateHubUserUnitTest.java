package com.thitsaworks.operation_portal.hubuser.domain.command;

import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.core.hubuser.command.CreateHubUser;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class CreateHubUserUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(CreateHubUserUnitTest.class);

    @Autowired
    private CreateHubUser createHubUser;

    @Test
    public void test_createHubUserSuccessfully() throws Exception {

        this.createHubUser.execute(
                new CreateHubUser.Input("Ingyin", new Email("Ingyin@thitsaworks.com"), "Ingyin", "thandar", "BA"));
    }

}
