package com.thitsaworks.operation_portal.usecase.hub_operator.It;

import com.thitsaworks.operation_portal.component.common.type.PrincipalStatus;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.usecase.HubOperatorUseCaseConfiguration;
import com.thitsaworks.operation_portal.usecase.hub_operator.BaseVaultSetUpTest;
import com.thitsaworks.operation_portal.usecase.hub_operator.CreateNewHubUser;
import com.thitsaworks.operation_portal.usecase.hub_operator.TestSettings;
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
                HubOperatorUseCaseConfiguration.class, TestSettings.class})
public class CreateNewHubUserIT extends BaseVaultSetUpTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateNewHubUserIT.class);

    @Autowired
    private CreateNewHubUser createNewHubUser;

    @Test
    public void success() throws DomainException {
        var input = new CreateNewHubUser.Input(
                "John Doe",
                new Email("njjaa@gmail.com"),
                "123456",
                "John",
                "Doe",
                "Manager",
                UserRoleType.ADMIN,
                PrincipalStatus.ACTIVE
        );
        var output = this.createNewHubUser.execute(input);
        LOGGER.info("HubUserId: {}, AccessKey: {}, SecretKey: {}", output.hubUserId(), output.accessKey(), output.secretKey());
    }
}
