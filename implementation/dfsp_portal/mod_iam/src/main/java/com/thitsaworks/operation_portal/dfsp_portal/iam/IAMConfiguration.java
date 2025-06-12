package com.thitsaworks.operation_portal.dfsp_portal.iam;

import com.thitsaworks.operation_portal.component.infra.hazelcast.HazelcastConfiguration;
import com.thitsaworks.operation_portal.component.infra.mysql.DfspPortalPersistenceConfiguration;
import com.thitsaworks.operation_portal.component.misc.MiscConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@ComponentScan("com.thitsaworks.operation_portal.iam")
@Import(value = {
        MiscConfiguration.class, HazelcastConfiguration.class, DfspPortalPersistenceConfiguration.class
})
public class IAMConfiguration {

}
