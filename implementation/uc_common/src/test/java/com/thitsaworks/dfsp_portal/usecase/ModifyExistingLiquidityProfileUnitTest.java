package com.thitsaworks.dfsp_portal.usecase;

import com.thitsaworks.dfsp_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.dfsp_portal.datasource.persistence.MySqlDbSettings;
import com.thitsaworks.dfsp_portal.participant.identity.LiquidityProfileId;
import com.thitsaworks.dfsp_portal.participant.identity.ParticipantId;
import com.thitsaworks.dfsp_portal.usecase.common.ModifyExistingLiquidityProfile;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;

@ContextConfiguration(classes = {CommonUseCaseConfiguration.class, MySqlDbSettings.class})
public class ModifyExistingLiquidityProfileUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyExistingLiquidityProfileUnitTest.class);

    @Autowired
    private ModifyExistingLiquidityProfile modifyExistingLiquidityProfile;

    @Test
    public void test_modifyExistingLiquidityProfileSuccessfully() throws Exception {

        List<ModifyExistingLiquidityProfile.Input.LiquidityProfileInfo> info = new ArrayList<>();
        info.add(new ModifyExistingLiquidityProfile.Input.LiquidityProfileInfo(
                new LiquidityProfileId(390914496700383232L), "NYEIN NYEIN EI", "030403043040953", "SGD", true));

        this.modifyExistingLiquidityProfile.execute(
                new ModifyExistingLiquidityProfile.Input(new ParticipantId(390906682871414784L), info));

    }

}
