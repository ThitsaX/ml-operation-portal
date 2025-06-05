package com.thitsaworks.dfsp_portal.participant.domain.query;

import com.thitsaworks.dfsp_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.dfsp_portal.participant.ParticipantConfiguration;
import com.thitsaworks.dfsp_portal.participant.domain.persistence.MySqlDbSettings;
import com.thitsaworks.dfsp_portal.participant.query.GetParticipants;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {ParticipantConfiguration.class, MySqlDbSettings.class})
public class GetParticipantsUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(GetParticipantsUnitTest.class);

    @Autowired
    private GetParticipants getParticipants;

    @Test
    public void test_getParticipantsSuccessfully() {

        GetParticipants.Output output = this.getParticipants.execute(new GetParticipants.Input());

        if (output.getParticipantInfoList() != null) {
            for (var obj : output.getParticipantInfoList())
                System.out.println(obj.getDfsp_code() + " , " +
                        obj.getName() + " , " +
                        obj.getAddress() + " , " +
                        obj.getMobile().getValue()
                        + " , " + obj.getBusinessContact()
                        + " , " + obj.getTechnicalContact()
                );
        } else {
            System.out.println("No record");
        }

    }

}
