package com.thitsaworks.operation_portal.core.iam.model.command;

import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.component.common.type.PrincipalStatus;
import com.thitsaworks.operation_portal.component.common.type.RealmType;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.infra.redis.RedisConfiguration;
import com.thitsaworks.operation_portal.core.iam.IAMConfiguration;
import com.thitsaworks.operation_portal.core.iam.command.CreatePrincipal;
import com.thitsaworks.operation_portal.core.iam.exception.DuplicatePrincipalException;
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
public class CreatePrincipalUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(CreatePrincipalUnitTest.class);

    @Autowired
    private CreatePrincipal createPrincipal;

    @Test
    public void test_createPrincipalSuccessfully() throws DuplicatePrincipalException {

        this.createPrincipal.execute(
                new CreatePrincipal.Input(new PrincipalId(1L),
                                          RealmType.PARTICIPANT,
                                          "password",
                                          new RealmId(395926063007432704L),
                                          UserRoleType.ADMIN, PrincipalStatus.ACTIVE));
    }

}
