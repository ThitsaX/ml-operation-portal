package com.thitsaworks.dfsp_portal.iam.domain.command;

import com.thitsaworks.dfsp_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.dfsp_portal.iam.IAMConfiguration;
import com.thitsaworks.dfsp_portal.iam.domain.UserRoleType;
import com.thitsaworks.dfsp_portal.iam.exception.DuplicatePrincipalException;
import com.thitsaworks.dfsp_portal.iam.identity.PrincipalId;
import com.thitsaworks.dfsp_portal.iam.identity.RealmId;
import com.thitsaworks.dfsp_portal.iam.type.PrincipalStatus;
import com.thitsaworks.dfsp_portal.iam.type.RealmType;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {IAMConfiguration.class})
public class CreatePrincipalUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(CreatePrincipalUnitTest.class);

    @Autowired
    private CreatePrincipal createPrincipal;

    @Test
    public void test_createPrincipalSuccessfully() throws DuplicatePrincipalException {

        this.createPrincipal.execute(
                new CreatePrincipal.Input(new PrincipalId(1L),
                        RealmType.PARTICIPANT,
                        "password",
                        new RealmId(395926063007432704L),
                        UserRoleType.ADMIN, PrincipalStatus.ACTIVE));
    }

}
