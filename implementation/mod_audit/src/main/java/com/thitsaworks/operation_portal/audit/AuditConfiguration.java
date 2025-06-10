package com.thitsaworks.operation_portal.audit;

import com.thitsaworks.operation_portal.component.ComponentConfiguration;
import com.thitsaworks.operation_portal.datasource.persistence.MySqlReadDbConfiguration;
import com.thitsaworks.operation_portal.datasource.persistence.MySqlWriteDbConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@ComponentScan("com.thitsaworks.operation_portal.audit")
@Import(value = {
        ComponentConfiguration.class, MySqlWriteDbConfiguration.class, MySqlReadDbConfiguration.class
})
public class AuditConfiguration {

}
