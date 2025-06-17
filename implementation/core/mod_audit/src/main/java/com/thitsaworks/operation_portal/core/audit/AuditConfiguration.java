package com.thitsaworks.operation_portal.core.audit;

import com.thitsaworks.operation_portal.component.infra.mysql.core.CorePersistenceConfiguration;
import com.thitsaworks.operation_portal.component.misc.MiscConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@ComponentScan("com.thitsaworks.operation_portal.core.audit")
@Import(value = {
        MiscConfiguration.class, CorePersistenceConfiguration.class
})
public class AuditConfiguration {

}
