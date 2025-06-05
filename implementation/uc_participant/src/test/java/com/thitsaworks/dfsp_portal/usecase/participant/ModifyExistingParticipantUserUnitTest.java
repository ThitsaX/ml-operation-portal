package com.thitsaworks.dfsp_portal.usecase.participant;

import com.thitsaworks.dfsp_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.dfsp_portal.component.type.Email;
import com.thitsaworks.dfsp_portal.datasource.persistence.MySqlDbSettings;
import com.thitsaworks.dfsp_portal.iam.domain.UserRoleType;
import com.thitsaworks.dfsp_portal.iam.type.PrincipalStatus;
import com.thitsaworks.dfsp_portal.participant.identity.ParticipantUserId;
import com.thitsaworks.dfsp_portal.usecase.ParticipantUseCaseConfiguration;
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
