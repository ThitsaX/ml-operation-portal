package com.thitsaworks.operation_portal.datasource.persistence;

import com.thitsaworks.operation_portal.vault.DfspVault;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

public class MySqlDbSettings {

    @Autowired
    private DfspVault dfspVault;

    @Bean
    public MySqlReadDbConfiguration.Settings mySqlReadDbConfigurationSettings() {

        MySqlReadDbConfiguration.Settings settings =
                this.dfspVault.get("mysql/read_db/settings",
                        MySqlReadDbConfiguration.Settings.class);

        return settings;
    }

    @Bean
    public MySqlWriteDbConfiguration.Settings mySqlWriteDbConfigurationSettings() {

        MySqlWriteDbConfiguration.Settings settings =
                this.dfspVault.get("mysql/write_db/settings",
                        MySqlWriteDbConfiguration.Settings.class);

        return settings;
    }

}