package com.thitsaworks.operation_portal.core.participant.model.repository;

import com.thitsaworks.operation_portal.component.common.type.Mobile;
import com.thitsaworks.operation_portal.component.common.type.ParticipantName;
import com.thitsaworks.operation_portal.component.common.type.ParticipantStatus;
import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.core.participant.model.Participant;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class ParticipantRepositoryUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(ParticipantRepositoryUnitTest.class);

    @Autowired
    private ParticipantRepository participantRepository;

    @Test
    public void test_createParticipantSuccessfully() {

        Participant participant = new Participant(1,
                                                  new ParticipantName("okdollar"),
                                                  "OK Dollar",
                                                  "Sanchaung Township, Yangon.",
                                                  new Mobile("+959400547258"),
                                                  ParticipantStatus.ACTIVE);

        this.participantRepository.save(participant);

    }

}
