package com.thitsaworks.operation_portal.api.hub_operator;

import com.thitsaworks.operation_portal.ledger.CentralLedgerConfiguration;
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
import java.util.NoSuchElementException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Configuration
@PropertySource(value = {"classpath:/dbsettings.properties"})
@Import(value = {
        HubOperatorUseCaseConfiguration.class, CommonUseCaseConfiguration.class,
        ParticipantUseCaseConfiguration.class, CentralLedgerConfiguration.class,
        WebConfiguration.class, WebSecurityConfiguration.class
})
public class DfspPortalHubOperatorApiConfiguration {

    private static final Logger logger =  LogManager.getLogger(DfspPortalHubOperatorApiConfiguration.class);

    @Autowired
    private DfspVault dfspVault;

    @Autowired
    private Environment env;

    @Bean
    public MySqlReadDbConfiguration.Settings mySqlReadDbConfigurationSettings() {

        MySqlReadDbConfiguration.Settings settings;

        try{

          logger.info("Trying to Access MySQL Settings 'mysql/read_db/settings' Data form Vault");
          settings = this.dfspVault.get("mysql/read_db/settings", MySqlReadDbConfiguration.Settings.class);

        }catch(Exception e){

          logger.error("An error occurred while accessing MySQL settings from the vault: " + e.getMessage());
          throw e;
        }

        logger.info("Success: Read 'mysql/read_db/settings'");

        try{
            logger.info("Trying to Access readDb.min/maxPool");

            settings.setMinPoolSize(Integer.parseInt(env.getRequiredProperty("readDb.minPool")));
            settings.setMaxPoolSize(Integer.parseInt(env.getRequiredProperty("readDb.maxPool")));

        } catch (NumberFormatException e) {
            logger.error("An error occurred while parsing pool size properties: " + e.getMessage());
            throw e;

        } catch (NoSuchElementException e) {
            logger.error("Required pool size properties not found: " + e.getMessage());
            throw e;

        }

        logger.info("Success: Read MySQL Settings from Vault.");
        return settings;
    }

    @Bean
    public MySqlWriteDbConfiguration.Settings mySqlWriteDbConfigurationSettings() {
        MySqlWriteDbConfiguration.Settings settings;

        try {
            logger.info("Trying to Access MySQL Settings 'mysql/write_db/settings' Data form Vault");

            settings = this.dfspVault.get("mysql/write_db/settings", MySqlWriteDbConfiguration.Settings.class);

        }catch(Exception e){
            logger.error("An error occurred while accessing MySQL settings from the vault: " + e.getMessage());
            throw e;
        }
        logger.info("Success: Reading 'mysql/write_db/settings'");

        try {
              logger.info("Trying to Read/Parsed writeDb.min/maxPool");

              settings.setMinPoolSize(Integer.parseInt(this.env.getRequiredProperty("writeDb.minPool")));
              settings.setMaxPoolSize(Integer.parseInt(this.env.getRequiredProperty("writeDb.maxPool")));
        } catch (NumberFormatException e) {
              logger.error("An error occurred while parsing pool size properties: " + e.getMessage());
              throw e;

        } catch (NoSuchElementException e) {
            logger.error("Required pool size properties not found: " + e.getMessage());
            throw e;

        }

        logger.info("Success: Accessed MySQL Settings from Vault.");
        return settings;
    }

    @Bean
    public CentralLedgerReadDbConfiguration.Settings centralLedgerReadDbConfigurationSettings() {

        CentralLedgerReadDbConfiguration.Settings settings;
        try {
            logger.info("Trying to Access Central Ledger Settings 'central_ledger/read_db/settings' Data form Vault");
        
            settings = this.dfspVault.get("central_ledger/read_db/settings", CentralLedgerReadDbConfiguration.Settings.class);

        } catch (Exception e) {
            logger.error("An error occurred while accessing Central Ledger settings from the vault: " + e.getMessage());
            throw e;
        }
        logger.info("Success: Accessed 'central_ledger/read_db/settings'");

        return settings;
    }

}
