package com.thitsaworks.dfsp_portal.usecase;

import com.thitsaworks.dfsp_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.dfsp_portal.component.type.Email;
import com.thitsaworks.dfsp_portal.component.type.Mobile;
import com.thitsaworks.dfsp_portal.datasource.persistence.MySqlDbSettings;
import com.thitsaworks.dfsp_portal.participant.identity.ContactId;
import com.thitsaworks.dfsp_portal.participant.identity.ParticipantId;
import com.thitsaworks.dfsp_portal.usecase.common.ModifyExistingContact;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;

@ContextConfiguration(classes = {CommonUseCaseConfiguration.class, MySqlDbSettings.class})
public class ModifyExistingContactUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyExistingContactUnitTest.class);

    @Autowired
    private ModifyExistingContact modifyExistingContact;

    @Test
    public void test_modifyExistingContactSuccessfully() throws Exception {

        List<ModifyExistingContact.Input.ContactInfo> contactInfoList = new ArrayList<>();
        contactInfoList.add(new ModifyExistingContact.Input.ContactInfo(
                new ContactId(407911993528320000L),
                "Phyu Phyu Thant",
                "Business Development Manager",
                new Email("ppt@gmail.com"),
                new Mobile("+959420784323")));

        this.modifyExistingContact.execute(new ModifyExistingContact.Input(
                new ParticipantId(390908599471210496L), contactInfoList));
    }

}
