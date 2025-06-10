package com.thitsaworks.operation_portal.iam;

import com.thitsaworks.operation_portal.component.ComponentConfiguration;
import com.thitsaworks.operation_portal.datasource.cache.HazelcastConfiguration;
import com.thitsaworks.operation_portal.datasource.persistence.MySqlReadDbConfiguration;
import com.thitsaworks.operation_portal.datasource.persistence.MySqlWriteDbConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@ComponentScan("com.thitsaworks.operation_portal.iam")
@Import(value = {
        ComponentConfiguration.class, HazelcastConfiguration.class, MySqlWriteDbConfiguration.class,
        MySqlReadDbConfiguration.class
})
public class IAMConfiguration {

}
