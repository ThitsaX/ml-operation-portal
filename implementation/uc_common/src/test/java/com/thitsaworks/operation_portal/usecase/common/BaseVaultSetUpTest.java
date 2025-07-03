package com.thitsaworks.operation_portal.usecase.common;

import com.thitsaworks.operation_portal.component.infra.flyway.DatabaseMigration;
import com.thitsaworks.operation_portal.component.infra.mysql.core.CoreDataSourceConfiguration;
import com.thitsaworks.operation_portal.component.infra.vault.VaultConfiguration;
import org.junit.jupiter.api.BeforeAll;

public abstract class BaseVaultSetUpTest {

    @BeforeAll
    public static void beforeAll() {

        System.setProperty("VAULT_ADDR", "http://127.0.0.1:8200");
        System.setProperty("VAULT_TOKEN", "hvs.8ab4SyPeDRqTFW1Pk9VyNNaN");
        System.setProperty("ENGINE_PATH", "operation_portal");

        DatabaseMigration.migrate(CoreDataSourceConfiguration.FLYWAY_MIGRATION,
                new VaultConfiguration.Settings(System.getProperty("VAULT_ADDR"), System.getProperty("VAULT_TOKEN"), System.getProperty("ENGINE_PATH")));
    }

}
