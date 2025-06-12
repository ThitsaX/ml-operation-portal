package com.thitsaworks.operation_portal.dfsp_portal.hubuser;

import com.thitsaworks.operation_portal.component.infra.mysql.DfspPortalPersistenceConfiguration;
import com.thitsaworks.operation_portal.component.misc.MiscConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@ComponentScan("com.thitsaworks.operation_portal.hubuser")
@Import(value = {
        MiscConfiguration.class, DfspPortalPersistenceConfiguration.class
})
public class HubUserConfiguration {

}
