package com.thitsaworks.operation_portal.core.iam.model.command;

import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.component.common.type.PrincipalStatus;
import com.thitsaworks.operation_portal.component.common.type.RealmType;
import com.thitsaworks.operation_portal.component.infra.redis.RedisConfiguration;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.iam.IAMConfiguration;
import com.thitsaworks.operation_portal.core.iam.command.CreatePrincipalCommand;
import com.thitsaworks.operation_portal.core.iam.model.TestSettings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {IAMConfiguration.class, TestSettings.class, RedisConfiguration.class})
public class CreatePrincipalCommandUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(CreatePrincipalCommandUnitTest.class);

    @Autowired
    private CreatePrincipalCommand createPrincipalCommand;

    @Test
    public void test_createPrincipalSuccessfully() throws DomainException {

        this.createPrincipalCommand.execute(
            new CreatePrincipalCommand.Input(new PrincipalId(1L),
                                             RealmType.PARTICIPANT,
                                             "password",
                                             new RealmId(395926063007432704L),
                                             PrincipalStatus.ACTIVE));
    }

}
