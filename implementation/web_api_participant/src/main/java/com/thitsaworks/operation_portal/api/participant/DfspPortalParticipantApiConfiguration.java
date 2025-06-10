package com.thitsaworks.operation_portal.api.participant;

import com.thitsaworks.operation_portal.usecase.CentralLedgerUseCaseConfiguration;
import com.thitsaworks.operation_portal.datasource.persistence.CentralLedgerReadDbConfiguration;
import com.thitsaworks.operation_portal.datasource.persistence.MySqlReadDbConfiguration;
import com.thitsaworks.operation_portal.datasource.persistence.MySqlWriteDbConfiguration;
import com.thitsaworks.operation_portal.usecase.CommonUseCaseConfiguration;
import com.thitsaworks.operation_portal.usecase.HubOperatorUseCaseConfiguration;
import com.thitsaworks.operation_portal.usecase.ParticipantUseCaseConfiguration;
import com.thitsaworks.operation_portal.vault.DfspVault;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource(value = {"classpath:/dbsettings.properties"})
@Import(value = {
        HubOperatorUseCaseConfiguration.class, CommonUseCaseConfiguration.class,
        ParticipantUseCaseConfiguration.class, CentralLedgerUseCaseConfiguration.class,
        WebConfiguration.class, WebSecurityConfiguration.class
})
public class DfspPortalParticipantApiConfiguration {

    @Autowired
    private DfspVault dfspVault;

    @Autowired
    private Environment env;

    @Bean
    public MySqlReadDbConfiguration.Settings mySqlReadDbConfigurationSettings() {

        MySqlReadDbConfiguration.Settings settings =
                this.dfspVault.get("mysql/read_db/settings", MySqlReadDbConfiguration.Settings.class);

        settings.setMinPoolSize(Integer.parseInt(env.getRequiredProperty("readDb.minPool")));
        settings.setMaxPoolSize(Integer.parseInt(env.getRequiredProperty("readDb.maxPool")));

        return settings;
    }

    @Bean
    public MySqlWriteDbConfiguration.Settings mySqlWriteDbConfigurationSettings() {

        MySqlWriteDbConfiguration.Settings settings =
                this.dfspVault.get("mysql/write_db/settings", MySqlWriteDbConfiguration.Settings.class);

        settings.setMinPoolSize(Integer.parseInt(this.env.getRequiredProperty("writeDb.minPool")));
        settings.setMaxPoolSize(Integer.parseInt(this.env.getRequiredProperty("writeDb.maxPool")));

        return settings;
    }

    @Bean
    public CentralLedgerReadDbConfiguration.Settings centralLedgerReadDbConfigurationSettings() {

        CentralLedgerReadDbConfiguration.Settings settings =
                this.dfspVault.get("central_ledger/read_db/settings",
                        CentralLedgerReadDbConfiguration.Settings.class);

        return settings;
    }

}
