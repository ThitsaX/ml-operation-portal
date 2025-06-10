package com.thitsaworks.operation_portal.usecase.hub_operator;

import com.thitsaworks.operation_portal.component.security.SecurityContext;
import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.component.usecase.UseCaseContext;
import com.thitsaworks.operation_portal.datasource.persistence.MySqlDbSettings;
import com.thitsaworks.operation_portal.iam.domain.UserRoleType;
import com.thitsaworks.operation_portal.iam.type.PrincipalStatus;
import com.thitsaworks.operation_portal.usecase.HubOperatorUseCaseConfiguration;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {HubOperatorUseCaseConfiguration.class, MySqlDbSettings.class})
public class CreateNewHubUserUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(CreateNewAnnouncementUnitTest.class);

    @Autowired
    private CreateNewHubUser createNewHubUser;

    @Test
    public void test_createNewHubUserSuccessfully() throws Exception {

        UseCaseContext.set(new SecurityContext("392628367895068672", "402814821686345728"));

        this.createNewHubUser.execute(
                new CreateNewHubUser.Input("aye chan", new Email("aye@gmail.com"), "ac123", "Aye", "Chan",
                        "Business Management", UserRoleType.SUPERUSER, PrincipalStatus.ACTIVE));

    }

}
