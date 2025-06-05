package com.thitsaworks.dfsp_portal.hubuser;

import com.thitsaworks.dfsp_portal.component.ComponentConfiguration;
import com.thitsaworks.dfsp_portal.datasource.persistence.MySqlReadDbConfiguration;
import com.thitsaworks.dfsp_portal.datasource.persistence.MySqlWriteDbConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@ComponentScan("com.thitsaworks.dfsp_portal.hubuser")
@Import(value = {
        ComponentConfiguration.class, MySqlWriteDbConfiguration.class, MySqlReadDbConfiguration.class
})
public class HubUserConfiguration {

}
