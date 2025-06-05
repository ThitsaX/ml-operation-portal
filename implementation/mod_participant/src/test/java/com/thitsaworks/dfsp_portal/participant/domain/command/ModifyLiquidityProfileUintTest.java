package com.thitsaworks.dfsp_portal.participant.domain.command;

import com.thitsaworks.dfsp_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.dfsp_portal.participant.ParticipantConfiguration;
import com.thitsaworks.dfsp_portal.participant.domain.persistence.MySqlDbSettings;
import com.thitsaworks.dfsp_portal.participant.identity.LiquidityProfileId;
import com.thitsaworks.dfsp_portal.participant.identity.ParticipantId;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {ParticipantConfiguration.class, MySqlDbSettings.class})
public class ModifyLiquidityProfileUintTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyLiquidityProfileUintTest.class);

    @Autowired
    private ModifyLiquidityProfile modifyLiquidityProfile;

    @Test
    public void test_modifyLiquidityProfileSuccessfully() throws Exception {

        this.modifyLiquidityProfile.execute(
                new ModifyLiquidityProfile.Input(new ParticipantId(390906682871414784L),
                        new LiquidityProfileId(390914496700383232L),
                        "NYEIN EI",
                        "4342255235654",
                        "MMK", true));
    }

}
