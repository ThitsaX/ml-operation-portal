package com.thitsaworks.operation_portal.core.test_iam.IT;

import org.junit.jupiter.api.BeforeAll;

public abstract class BaseVaultSetUpTest {

    @BeforeAll
    public static void beforeAll() {

        System.setProperty("VAULT_ADDR", "http://127.0.0.1:8200");
        System.setProperty("VAULT_TOKEN", "hvs.WJRD6crpVfPSXrEZMkAarFT8");
        System.setProperty("ENGINE_PATH", "operation_portal");

//        DatabaseMigration.migrate(CoreDataSourceConfiguration.FLYWAY_MIGRATION,
//                new VaultConfiguration.Settings(System.getProperty("VAULT_ADDR"), System.getProperty("VAULT_TOKEN"), System.getProperty("ENGINE_PATH")));
    }

}
