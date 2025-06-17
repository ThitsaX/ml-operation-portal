package com.thitsaworks.operation_portal.usecase;

import com.thitsaworks.operation_portal.component.exception.DFSPPortalException;
import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.datasource.persistence.MySqlDbSettings;
import com.thitsaworks.operation_portal.iam.domain.UserRoleType;
import com.thitsaworks.operation_portal.iam.type.PrincipalStatus;
import com.thitsaworks.operation_portal.iam.type.RealmType;
import com.thitsaworks.operation_portal.participant.identity.ParticipantId;
import com.thitsaworks.operation_portal.usecase.common.CreateNewParticipantUser;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {CommonUseCaseConfiguration.class, MySqlDbSettings.class})
public class CreateNewParticipantUserUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(CreateNewParticipantUserUnitTest.class);

    @Autowired
    private CreateNewParticipantUser createNewParticipantUser;

    @Test
    public void test_createNewParticipantUserSuccessfully() throws DFSPPortalException {

        this.createNewParticipantUser.execute(new CreateNewParticipantUser.Input(
                "hein", new Email("akh@gmail.com"), "Akh12345", "Aung Kyaw",
                "Hein", "IT Manager", new ParticipantId(395926063007432704L), UserRoleType.SUPERUSER,
                RealmType.PARTICIPANT, PrincipalStatus.ACTIVE));

    }

}
