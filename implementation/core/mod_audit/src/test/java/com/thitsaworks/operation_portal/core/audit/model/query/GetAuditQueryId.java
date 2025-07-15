package com.thitsaworks.operation_portal.core.audit.model.query;

import com.thitsaworks.operation_portal.core.audit.AuditConfiguration;
import com.thitsaworks.operation_portal.core.audit.model.BaseVaultSetUpTest;
import com.thitsaworks.operation_portal.core.audit.model.TestSettings;
import com.thitsaworks.operation_portal.core.audit.query.AuditQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AuditConfiguration.class, TestSettings.class})
public class GetAuditQueryId extends BaseVaultSetUpTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetAuditQueryId.class);

    @Autowired
    private AuditQuery auditQuery;

    @Test
    public void testGetAuditById() {
        var auditData = auditQuery.getAudits();

        LOGGER.info("Audit Data: {}", auditData);

    }


}
