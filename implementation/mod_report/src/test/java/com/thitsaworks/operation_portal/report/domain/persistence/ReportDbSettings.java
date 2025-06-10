package com.thitsaworks.operation_portal.report.domain.persistence;

import com.thitsaworks.operation_portal.datasource.persistence.CentralLedgerReadDbConfiguration;
import com.thitsaworks.operation_portal.datasource.persistence.MySqlReadDbConfiguration;
import com.thitsaworks.operation_portal.vault.DfspVault;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

public class ReportDbSettings {

    @Autowired
    private DfspVault dfspVault;

    @Bean
    public CentralLedgerReadDbConfiguration.Settings centralLedgerReadDbConfigurationSettings() {

        CentralLedgerReadDbConfiguration.Settings settings =
                this.dfspVault.get("central_ledger/read_db/settings",
                        CentralLedgerReadDbConfiguration.Settings.class);

        return settings;
    }

    @Bean
    public MySqlReadDbConfiguration.Settings mySqlReadDbConfigurationSettings() {

        MySqlReadDbConfiguration.Settings settings =
                this.dfspVault.get("mysql/read_db/settings",
                        MySqlReadDbConfiguration.Settings.class);

        return settings;
    }



}