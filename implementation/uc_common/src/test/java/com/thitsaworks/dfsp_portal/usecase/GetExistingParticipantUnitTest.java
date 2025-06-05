package com.thitsaworks.dfsp_portal.usecase;

import com.thitsaworks.dfsp_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.dfsp_portal.datasource.persistence.MySqlDbSettings;
import com.thitsaworks.dfsp_portal.participant.identity.ParticipantId;
import com.thitsaworks.dfsp_portal.usecase.common.GetExistingParticipant;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {CommonUseCaseConfiguration.class, MySqlDbSettings.class})
public class GetExistingParticipantUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(GetExistingParticipantUnitTest.class);

    @Autowired
    private GetExistingParticipant getExistingParticipant;

    @Test
    public void test_getExistingParticipantSuccessfully() throws Exception {

        var output = this.getExistingParticipant.execute(
                new GetExistingParticipant.Input(new ParticipantId(390908599471210496L)));

        if (output != null) {

            LOG.info("Name : " + output.getName() +
                    ", ParticipantId : " + output.getParticipantId().getId().toString());
            for (var obj : output.getContactInfoList()) {
                LOG.info(" Name : " + obj.getName() +
                        " , contact_type :  " + obj.getContactType());
            }
        } else {
            LOG.info("No record");
        }
    }

}
