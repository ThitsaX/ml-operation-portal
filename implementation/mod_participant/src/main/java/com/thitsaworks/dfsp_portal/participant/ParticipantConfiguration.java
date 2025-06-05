package com.thitsaworks.dfsp_portal.participant;

import com.thitsaworks.dfsp_portal.component.ComponentConfiguration;
import com.thitsaworks.dfsp_portal.datasource.cache.HazelcastConfiguration;
import com.thitsaworks.dfsp_portal.datasource.persistence.MySqlReadDbConfiguration;
import com.thitsaworks.dfsp_portal.datasource.persistence.MySqlWriteDbConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@ComponentScan("com.thitsaworks.dfsp_portal.participant")
@Import(value = {
        ComponentConfiguration.class, HazelcastConfiguration.class, MySqlWriteDbConfiguration.class,
        MySqlReadDbConfiguration.class})
public class ParticipantConfiguration {

}
