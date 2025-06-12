package com.thitsaworks.operation_portal.dfsp_portal.audit;

import com.thitsaworks.operation_portal.component.infra.mysql.DfspPortalPersistenceConfiguration;
import com.thitsaworks.operation_portal.component.misc.MiscConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@ComponentScan("com.thitsaworks.operation_portal.dfsp_portal.audit")
@Import(value = {
        MiscConfiguration.class, DfspPortalPersistenceConfiguration.class
})
public class AuditConfiguration {

}
