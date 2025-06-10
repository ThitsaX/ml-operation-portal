package com.thitsaworks.operation_portal.usecase;

import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.component.type.Mobile;
import com.thitsaworks.operation_portal.datasource.persistence.MySqlDbSettings;
import com.thitsaworks.operation_portal.participant.identity.ParticipantId;
import com.thitsaworks.operation_portal.usecase.common.ModifyExistingParticipant;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {CommonUseCaseConfiguration.class, MySqlDbSettings.class})
public class ModifyExistingParticipantUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyExistingParticipantUnitTest.class);

    @Autowired
    private ModifyExistingParticipant modifyExistingParticipant;

    @Test
    public void test_modifyExistingParticipantSuccessfully() throws Exception {

        this.modifyExistingParticipant.execute(new ModifyExistingParticipant.Input(
                new ParticipantId(390908599471210496L),"abc", "No.30, Sanchaung Township",
                new Mobile("+9592174595"), null, null, null));

    }

}
