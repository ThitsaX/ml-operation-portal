package com.thitsaworks.operation_portal.component.infra.flyway;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlywayMigration {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlywayMigration.class);

    public static void migrate(Settings settings) {

        Flyway.configure()
              .dataSource(settings.url(), settings.username(),
                          settings.password())
              .locations(settings.locations())
              .baselineOnMigrate(true)
              .load()
              .migrate();

    }

    public record Settings(String url, String username, String password, String... locations) { }

}
