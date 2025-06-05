package com.thitsaworks.dfsp_portal.usecase;

import com.thitsaworks.dfsp_portal.component.exception.DFSPPortalException;
import com.thitsaworks.dfsp_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.dfsp_portal.component.type.Email;
import com.thitsaworks.dfsp_portal.component.type.Mobile;
import com.thitsaworks.dfsp_portal.datasource.persistence.MySqlDbSettings;
import com.thitsaworks.dfsp_portal.participant.identity.ParticipantId;
import com.thitsaworks.dfsp_portal.usecase.common.CreateNewContact;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;

@ContextConfiguration(classes = {CommonUseCaseConfiguration.class, MySqlDbSettings.class})
public class CreateNewContactUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(CreateNewContactUnitTest.class);

    @Autowired
    private CreateNewContact createNewContact;

    @Test
    public void test_createNewContactSuccessfully() throws DFSPPortalException {

        List<CreateNewContact.Input.ContactInfo> contactInfoList = new ArrayList<>();

        contactInfoList.add(new CreateNewContact.Input.ContactInfo("Phyu Thant", "Business Development",
                new Email("phyu@gmail.com"), new Mobile("+959420784323"), "Business"));

        this.createNewContact.execute(
                new CreateNewContact.Input(new ParticipantId(390908599471210496L), contactInfoList));

    }

}
