package com.thitsaworks.operation_portal.usecase.participant;

import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.datasource.persistence.MySqlDbSettings;
import com.thitsaworks.operation_portal.iam.domain.UserRoleType;
import com.thitsaworks.operation_portal.iam.type.PrincipalStatus;
import com.thitsaworks.operation_portal.participant.identity.ParticipantUserId;
import com.thitsaworks.operation_portal.usecase.ParticipantUseCaseConfiguration;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {ParticipantUseCaseConfiguration.class, MySqlDbSettings.class})
public class ModifyExistingParticipantUserUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyExistingParticipantUserUnitTest.class);

    @Autowired
    private ModifyExistingParticipantUser modifyExistingParticipantUser;

    @Test
    public void test_modifyExistingUserSuccessfully() throws Exception {

        this.modifyExistingParticipantUser.execute(new ModifyExistingParticipantUser.Input(
                new ParticipantUserId(403544710259363840L), "kimseonho", new Email("kimseonho@sds.com"), "Kim",
                "SeonHo", "Actor", UserRoleType.valueOf("ADMIN"),PrincipalStatus.ACTIVE));

    }

}
