package com.thitsaworks.operation_portal.core.participant.model.command;

import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.core.participant.command.ModifyLiquidityProfile;
import com.thitsaworks.operation_portal.component.common.identifier.LiquidityProfileId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

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
