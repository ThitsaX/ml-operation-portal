package com.thitsaworks.operation_portal.dfsp_portal.participant.domain.command;

import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.dfsp_portal.participant.identity.ParticipantId;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class CreateLiquidityProfileUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(CreateLiquidityProfileUnitTest.class);

    @Autowired
    private CreateLiquidityProfile createLiquidityProfile;

    @Test
    public void test_createLiquidityProfileSuccessfully() throws Exception {

        this.createLiquidityProfile.execute(
                new CreateLiquidityProfile.Input(new ParticipantId(390906682871414784L), "NYEIN NYEIN EI",
                                                 "2341895948958343",
                                                 "MMK", true
                ));
    }

}
