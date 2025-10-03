package com.thitsaworks.operation_portal.core.audit.model.query;

import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.core.audit.AuditConfiguration;
import com.thitsaworks.operation_portal.core.audit.model.BaseVaultSetUpTest;
import com.thitsaworks.operation_portal.core.audit.model.TestSettings;
import com.thitsaworks.operation_portal.core.audit.query.GetAuditByParticipantAndUserQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AuditConfiguration.class, TestSettings.class})
public class GetAuditByParticipantAndUserQueryUnitTest extends BaseVaultSetUpTest {

    private static final Logger LOG = LoggerFactory.getLogger(GetAuditByParticipantAndUserQueryUnitTest.class);

    @Autowired
    private GetAuditByParticipantAndUserQuery getAuditByParticipantAndUserQuery;

    @Test
    public void test_getAuditsSuccessfully() {

        GetAuditByParticipantAndUserQuery.Output
            output =
            this.getAuditByParticipantAndUserQuery.execute(new GetAuditByParticipantAndUserQuery.Input(null,
                                                                                                       new UserId(1111111111111111L),
                                                                                                       Instant.parse(
                                                                                                           "2025-02-01T00:00:00Z"),
                                                                                                       Instant.parse(
                                                                                                           "2025-08-31T23:59:59Z"),
                                                                                                       null,
                    1,
                    10
            ));

        LOG.info("[{}]", output.getAuditInfoList());
    }

}

