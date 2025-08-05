package com.thitsaworks.operation_portal.core.test_iam.IT.command;

import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.type.PrincipalStatus;
import com.thitsaworks.operation_portal.component.common.type.RealmType;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.core.test_iam.IAMTestConfiguration;
import com.thitsaworks.operation_portal.core.test_iam.IT.BaseVaultSetUpTest;
import com.thitsaworks.operation_portal.core.test_iam.IT.TestSettings;
import com.thitsaworks.operation_portal.core.test_iam.command.CreateUserCommand;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;
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
        IAMTestConfiguration.class, TestSettings.class})
public class CreateUserCommandIT extends BaseVaultSetUpTest {

    private  final Logger LOGGER=LoggerFactory.getLogger(CreateUserCommandIT.class);

    @Autowired
    private CreateUserCommand createUserCommand;

    @Test
    public void success() throws IAMException {
        var result =this.createUserCommand.execute(new CreateUserCommand.Input(new UserId(22L),
                                                                               RealmType.PARTICIPANT,
                                                                               "222Jjaj",
                                                                               new RealmId(222L),
                                                                               UserRoleType.ADMIN,
                                                   PrincipalStatus.valueOf("Active")));

        LOGGER.info("AccessKey {},SecreKey {}",result.accessKey(),result.accessKey());
    }
}
