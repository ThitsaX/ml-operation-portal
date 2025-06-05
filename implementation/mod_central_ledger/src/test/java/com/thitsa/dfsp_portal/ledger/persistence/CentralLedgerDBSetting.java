package com.thitsa.dfsp_portal.ledger.persistence;

import com.thitsaworks.dfsp_portal.datasource.persistence.CentralLedgerReadDbConfiguration;
import com.thitsaworks.dfsp_portal.vault.DfspVault;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

public class CentralLedgerDBSetting {

    @Autowired
    private DfspVault dfspVault;

    @Bean
    public CentralLedgerReadDbConfiguration.Settings centralLedgerReadDbConfigurationSettings() {

        CentralLedgerReadDbConfiguration.Settings settings =
                this.dfspVault.get("central_ledger/read_db/settings",
                        CentralLedgerReadDbConfiguration.Settings.class);

        return settings;
    }

}
