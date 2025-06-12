package com.thitsaworks.operation_portal.dfsp_portal.participant.domain.repository;

import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.component.type.Mobile;
import com.thitsaworks.operation_portal.dfsp_portal.participant.domain.Participant;
import com.thitsaworks.operation_portal.dfsp_portal.participant.type.DfspCode;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

public class ParticipantRepositoryUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(ParticipantRepositoryUnitTest.class);

    @Autowired
    private ParticipantRepository participantRepository;

    @Test
    public void test_createParticipantSuccessfully() {

        Participant participant = new Participant(new DfspCode("okdollar"), "OK Dollar", null , "Sanchaung Township, Yangon.",
                                                  new Mobile("+959400547258"));
        this.participantRepository.save(participant);

    }

}
