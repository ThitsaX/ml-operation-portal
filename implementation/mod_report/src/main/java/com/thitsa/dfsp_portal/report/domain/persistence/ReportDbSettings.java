package com.thitsa.dfsp_portal.report.domain.persistence;

import com.thitsaworks.dfsp_portal.datasource.persistence.CentralLedgerReadDbConfiguration;
import com.thitsaworks.dfsp_portal.datasource.persistence.MySqlWriteDbConfiguration;
import com.thitsaworks.dfsp_portal.vault.DfspVault;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

public class ReportDbSettings {

    @Autowired
    private DfspVault dfspVault;

    @Bean
    public CentralLedgerReadDbConfiguration.Settings reportReadDbConfigurationSettings() {

        CentralLedgerReadDbConfiguration.Settings settings =
                this.dfspVault.get("central_ledger/read_db/setting",
                        CentralLedgerReadDbConfiguration.Settings.class);

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