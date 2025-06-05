package com.thitsaworks.dfsp_portal.usecase.query;

import com.thitsaworks.dfsp_portal.component.security.SecurityContext;
import com.thitsaworks.dfsp_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.dfsp_portal.component.usecase.UseCaseContext;
import com.thitsaworks.dfsp_portal.datasource.persistence.MySqlDbSettings;
import com.thitsaworks.dfsp_portal.usecase.HubOperatorUseCaseConfiguration;
import com.thitsaworks.dfsp_portal.usecase.hub_operator.GetAllParticipant;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {HubOperatorUseCaseConfiguration.class, MySqlDbSettings.class})
public class GetAllParticipantUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllParticipantUnitTest.class);

    @Autowired
    private GetAllParticipant getAllParticipant;

    @Test
    public void test_getAllParticipantSuccessfully() throws Exception {

        UseCaseContext.set(new SecurityContext("392628367895068672", "402814821686345728"));

        GetAllParticipant.Output output = this.getAllParticipant.execute(new GetAllParticipant.Input());

        if (output.getParticipantInfoList() != null && output.getParticipantInfoList().size() > 0) {
            for (var obj : output.getParticipantInfoList())
                LOG.info(obj.getParticipantId().getId() + ", " + obj.getName().toString());
        } else {
            LOG.info("No record");
        }
    }

}
