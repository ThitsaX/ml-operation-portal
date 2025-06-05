package com.thitsaworks.dfsp_portal.usecase;

import com.thitsaworks.dfsp_portal.component.exception.DFSPPortalException;
import com.thitsaworks.dfsp_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.dfsp_portal.datasource.persistence.MySqlDbSettings;
import com.thitsaworks.dfsp_portal.participant.identity.ParticipantId;
import com.thitsaworks.dfsp_portal.usecase.common.CreateNewLiquidityProfile;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;

@ContextConfiguration(classes = {CommonUseCaseConfiguration.class, MySqlDbSettings.class})
public class CreateNewLiquidityProfileUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(CreateNewLiquidityProfileUnitTest.class);

    @Autowired
    private CreateNewLiquidityProfile createNewLiquidityProfile;

    @Test
    public void test_createNewLiquidityProfileSuccessfully() throws DFSPPortalException {

        List<CreateNewLiquidityProfile.Input.LiquidityProfileInfo> liquidityProfileInfoList = new ArrayList<>();

        liquidityProfileInfoList.add(
                new CreateNewLiquidityProfile.Input.LiquidityProfileInfo("NYEIN NYEIN", "342805409503905", "USD",
                        true));

        this.createNewLiquidityProfile.execute(
                new CreateNewLiquidityProfile.Input(new ParticipantId(390908599471210496L), liquidityProfileInfoList));

    }

}
