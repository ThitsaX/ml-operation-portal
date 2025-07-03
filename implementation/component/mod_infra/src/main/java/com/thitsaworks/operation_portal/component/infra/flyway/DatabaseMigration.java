package com.thitsaworks.operation_portal.component.infra.flyway;


import com.thitsaworks.operation_portal.component.infra.vault.Vault;
import com.thitsaworks.operation_portal.component.infra.vault.VaultConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseMigration {

    private static  final Logger LOGGER = LoggerFactory.getLogger(DatabaseMigration.class);

    public static void migrate(String vaultPath, VaultConfiguration.Settings vaultSettings) {

        LOGGER.info("vaultSettings : <{}>", vaultSettings);
        var vault = new Vault(vaultSettings.address(), vaultSettings.token(), vaultSettings.enginePath());

        LOGGER.info("Done loading Vault with vaultSettings : <{}>", vaultSettings);
        DatabaseMigration.migrate(vault.get(vaultPath, FlywayMigration.Settings.class));

    }

    public static void migrate(FlywayMigration.Settings settings) {

        LOGGER.info("Migrating database...");
        FlywayMigration.migrate(settings);
        LOGGER.info("Done migrating database...");
    }

}
