package com.thitsaworks.operation_portal.core.audit.model.query;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.core.audit.AuditConfiguration;
import com.thitsaworks.operation_portal.core.audit.model.BaseVaultSetUpTest;
import com.thitsaworks.operation_portal.core.audit.model.TestSettings;
import com.thitsaworks.operation_portal.core.audit.query.GetAllAuditByParticipantQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.util.ArrayList;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
    classes = {
        AuditConfiguration.class, TestSettings.class})
public class GetAllAuditByParticipantQueryIT extends BaseVaultSetUpTest {
    private static final Logger LOG = LoggerFactory.getLogger(GetAllAuditByParticipantQueryIT.class);

    @Autowired
    private GetAllAuditByParticipantQuery getAllAuditByParticipantQuery;

    @Test
    public void test_getAuditsSuccessfully() {

        var grantedActionList = new ArrayList<ActionId>();

        GetAllAuditByParticipantQuery.Input input = new GetAllAuditByParticipantQuery.Input(
            new RealmId(731147922761035776L),
            Instant.parse("2025-02-01T00:00:00Z"),
            Instant.parse("2025-08-31T23:59:59Z"),
                          grantedActionList);

        GetAllAuditByParticipantQuery.Output output = getAllAuditByParticipantQuery.execute(input);

        LOG.info("Audit Info : {}", output.auditInfoList());

    }

}
