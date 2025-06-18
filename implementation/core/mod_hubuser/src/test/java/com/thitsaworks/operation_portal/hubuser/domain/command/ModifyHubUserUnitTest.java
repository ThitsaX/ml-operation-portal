package com.thitsaworks.operation_portal.hubuser.domain.command;

import com.thitsaworks.operation_portal.component.common.identifier.HubUserId;
import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.core.hubuser.command.ModifyHubUser;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class ModifyHubUserUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyHubUserUnitTest.class);

    @Autowired
    private ModifyHubUser modifyHubUser;

    @Test
    public void test_modifyHubUserSuccessfully() throws Exception {

        this.modifyHubUser.execute(
                new ModifyHubUser.Input(new HubUserId(410363288277254144L), "Ingyin",
                        "Ingyin Thandar", "Khaing", "BA"));
    }

}
