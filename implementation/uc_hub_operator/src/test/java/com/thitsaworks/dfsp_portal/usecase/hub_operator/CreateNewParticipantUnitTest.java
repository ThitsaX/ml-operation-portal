package com.thitsaworks.dfsp_portal.usecase.hub_operator;

import com.thitsaworks.dfsp_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.dfsp_portal.component.type.Email;
import com.thitsaworks.dfsp_portal.component.type.Mobile;
import com.thitsaworks.dfsp_portal.datasource.persistence.MySqlDbSettings;
import com.thitsaworks.dfsp_portal.participant.type.DfspCode;
import com.thitsaworks.dfsp_portal.usecase.HubOperatorUseCaseConfiguration;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;

@ContextConfiguration(classes = {HubOperatorUseCaseConfiguration.class, MySqlDbSettings.class})
public class CreateNewParticipantUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(CreateNewParticipantUnitTest.class);

    @Autowired
    private CreateNewParticipant createNewParticipant;

    @Test
    public void test_createNewParticipantSuccessfully() throws Exception {

        List<CreateNewParticipant.Input.ContactInfo> contactInfoList = new ArrayList<>();
        contactInfoList.add(
                new CreateNewParticipant.Input.ContactInfo("Kyalsin Nyein Chan", "Busineess Development",
                        new Email("thawdar@gmail.com"), new Mobile("+959400547249"), "Business"));

        this.createNewParticipant.execute(new CreateNewParticipant.Input(
                "test_mfi",
                new DfspCode("defg"),"abc",
                "No.30, Ahlone Township",
                new Mobile("+959250661838"),
                contactInfoList,null));

    }

}
