package com.thitsaworks.operation_portal.usecase.operation_portal.It;

import com.thitsaworks.operation_portal.component.misc.security.SecurityContext;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCaseContext;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCaseConfiguration;
import com.thitsaworks.operation_portal.usecase.operation_portal.SyncHubParticipantsToPortal;
import com.thitsaworks.operation_portal.usecase.operation_portal.TestSettings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {
            OperationPortalUseCaseConfiguration.class, TestSettings.class})
public class SyncHubParticipantsToPortalIT {

    private static final Logger LOG = LoggerFactory.getLogger(SyncHubParticipantsToPortalIT.class);

    @Autowired
    SyncHubParticipantsToPortal syncHubParticipantsToPortal;

    @Test
    public void test_syncParticipantsSuccessfully() throws Exception {

        SecurityContext securityContext = new SecurityContext(1111111111111111L, null, 411194012689530880L);

        UseCaseContext.set(securityContext);

        SyncHubParticipantsToPortal.Output output =
                this.syncHubParticipantsToPortal.execute(new SyncHubParticipantsToPortal.Input());

        LOG.info("Synced : [{}]", output.synced());

    }

}
