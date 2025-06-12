package com.thitsaworks.operation_portal.dfsp_portal.participant.domain.query;

import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.dfsp_portal.participant.exception.ParticipantNotFoundException;
import com.thitsaworks.operation_portal.dfsp_portal.participant.identity.ParticipantId;
import com.thitsaworks.operation_portal.dfsp_portal.participant.query.GetParticipant;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

public class GetParticipantUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(GetParticipantUnitTest.class);

    @Autowired
    private GetParticipant getParticipant;

    @Test
    public void test_getParticipantSuccessfully() throws ParticipantNotFoundException {

        GetParticipant.Output output =
                this.getParticipant.execute(new GetParticipant.Input(new ParticipantId(390906682871414784L)));

        if (output != null) {
            LOG.info(output.getDfsp_code() + " , " +
                    output.getName() + " , " +
                    output.getAddress() + " , " +
                    output.getMobile().getValue()
                    + " , " + output.getBusinessContactId().getId().toString()
                    + " , " + output.getTechnicalContactId().getId().toString()
            );

        } else {
            LOG.info("No record");
        }

    }

}
