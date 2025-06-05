package com.thitsaworks.dfsp_portal.participant.domain.repository;

import com.thitsaworks.dfsp_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.dfsp_portal.component.type.Mobile;
import com.thitsaworks.dfsp_portal.participant.ParticipantConfiguration;
import com.thitsaworks.dfsp_portal.participant.domain.Participant;
import com.thitsaworks.dfsp_portal.participant.domain.persistence.MySqlDbSettings;
import com.thitsaworks.dfsp_portal.participant.type.DfspCode;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {ParticipantConfiguration.class, MySqlDbSettings.class})
public class ParticipantRepositoryUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(ParticipantRepositoryUnitTest.class);

    @Autowired
    private ParticipantRepository participantRepository;

    @Test
    public void test_createParticipantSuccessfully() {

        Participant participant = new Participant(new DfspCode("okdollar"), "OK Dollar",null ,"Sanchaung Township, Yangon.",
                new Mobile("+959400547258"));
        this.participantRepository.save(participant);

    }

}
